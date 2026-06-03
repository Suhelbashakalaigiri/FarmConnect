package com.farmconnect.visitservice.service.impl;

import com.farmconnect.visitservice.dto.*;
import com.farmconnect.visitservice.entity.Visit;
import com.farmconnect.visitservice.enums.InspectionStatus;
import com.farmconnect.visitservice.enums.VisitStatus;
import com.farmconnect.visitservice.exception.*;
import com.farmconnect.visitservice.feign.BidServiceClient;
import com.farmconnect.visitservice.feign.BuyerServiceClient;
import com.farmconnect.visitservice.feign.CropServiceClient;
import com.farmconnect.visitservice.feign.FarmerServiceClient;
import com.farmconnect.visitservice.mapper.VisitMapper;
import com.farmconnect.visitservice.repository.VisitRepository;
import com.farmconnect.visitservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final BidServiceClient bidServiceClient;
    private final BuyerServiceClient buyerServiceClient;
    private final FarmerServiceClient farmerServiceClient;
    private final CropServiceClient cropServiceClient;

    @Override
    @Transactional
    public VisitResponse scheduleVisit(CreateVisitRequest request) {
        log.info("Scheduling visit for bid: {}", request.bidId());

        // 1. Validate Bid
        ApiResponse<ExternalBidResponse> bidResp = bidServiceClient.getBidById(request.bidId());
        if (!bidResp.success() || bidResp.data() == null) {
            throw new ResourceNotFoundException("Bid not found with ID: " + request.bidId());
        }
        if (!"VISIT_PENDING".equalsIgnoreCase(bidResp.data().bidStatus())) {
            throw new VisitNotAllowedException("Visit can only be scheduled if bid status is VISIT_PENDING. Current: " + bidResp.data().bidStatus());
        }

        // 2. Validate Buyer
        ApiResponse<ExternalBuyerResponse> buyerResp = buyerServiceClient.getBuyerById(request.buyerId());
        if (!buyerResp.success() || buyerResp.data() == null || !"ACTIVE".equalsIgnoreCase(buyerResp.data().status())) {
            throw new BusinessValidationException("Only ACTIVE buyers can participate in visits.");
        }

        // 3. Validate Farmer
        ApiResponse<ExternalFarmerResponse> farmerResp = farmerServiceClient.getFarmerById(request.farmerId());
        if (!farmerResp.success() || farmerResp.data() == null || !"ACTIVE".equalsIgnoreCase(farmerResp.data().status())) {
            throw new BusinessValidationException("Only ACTIVE farmers can participate in visits.");
        }

        // 4. Validate Crop
        ApiResponse<ExternalCropResponse> cropResp = cropServiceClient.getCropById(request.cropId());
        if (!cropResp.success() || cropResp.data() == null || !"AVAILABLE".equalsIgnoreCase(cropResp.data().status())) {
            throw new BusinessValidationException("Crop must be AVAILABLE for visit.");
        }

        // 5. Check if visit already scheduled
        if (visitRepository.existsByBidId(request.bidId())) {
            throw new VisitSchedulingException("Visit already scheduled for this bid.");
        }

        Visit visit = Visit.builder()
                .bidId(request.bidId())
                .cropId(request.cropId())
                .buyerId(request.buyerId())
                .farmerId(request.farmerId())
                .visitDate(request.visitDate())
                .visitStatus(VisitStatus.SCHEDULED)
                .inspectionStatus(InspectionStatus.PENDING)
                .build();

        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    public VisitResponse getVisitById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));
        return visitMapper.toResponse(visit);
    }

    @Override
    public List<VisitResponse> getVisitsByBuyer(Long buyerId) {
        return visitMapper.toResponseList(visitRepository.findByBuyerId(buyerId));
    }

    @Override
    public List<VisitResponse> getVisitsByFarmer(Long farmerId) {
        return visitMapper.toResponseList(visitRepository.findByFarmerId(farmerId));
    }

    @Override
    public VisitResponse getVisitByBid(Long bidId) {
        Visit visit = visitRepository.findByBidId(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found for bid ID: " + bidId));
        return visitMapper.toResponse(visit);
    }

    @Override
    @Transactional
    public VisitResponse rescheduleVisit(Long id, RescheduleVisitRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));

        if (visit.getVisitStatus() == VisitStatus.CANCELLED || visit.getVisitStatus() == VisitStatus.COMPLETED) {
            throw new BusinessValidationException("Cannot reschedule a " + visit.getVisitStatus() + " visit.");
        }

        visit.setVisitDate(request.newVisitDate());
        visit.setVisitStatus(VisitStatus.RESCHEDULED);
        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    @Transactional
    public VisitResponse markVisited(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));

        if (visit.getVisitStatus() != VisitStatus.SCHEDULED && visit.getVisitStatus() != VisitStatus.RESCHEDULED) {
            throw new BusinessValidationException("Only SCHEDULED or RESCHEDULED visits can be marked as VISITED.");
        }

        visit.setVisitStatus(VisitStatus.VISITED);
        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    @Transactional
    public VisitResponse approveInspection(Long id, InspectionApprovalRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));

        if (visit.getVisitStatus() != VisitStatus.VISITED) {
            throw new BusinessValidationException("Buyer can approve crop only after visit is completed (status: VISITED).");
        }

        if (visit.getInspectionStatus() != InspectionStatus.PENDING) {
            throw new InspectionAlreadyCompletedException("Inspection decision already recorded.");
        }

        visit.setInspectionStatus(InspectionStatus.APPROVED);
        visit.setVisitStatus(VisitStatus.COMPLETED);
        visit.setBuyerRemarks(request.remarks());
        visit.setApprovedAt(LocalDateTime.now());

        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    @Transactional
    public VisitResponse rejectInspection(Long id, InspectionRejectionRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));

        if (visit.getVisitStatus() != VisitStatus.VISITED) {
            throw new BusinessValidationException("Buyer can reject crop only after visit is completed (status: VISITED).");
        }

        if (visit.getInspectionStatus() != InspectionStatus.PENDING) {
            throw new InspectionAlreadyCompletedException("Inspection decision already recorded.");
        }

        visit.setInspectionStatus(InspectionStatus.REJECTED);
        visit.setVisitStatus(VisitStatus.COMPLETED);
        visit.setBuyerRemarks(request.remarks());

        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    @Transactional
    public VisitResponse cancelVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with ID: " + id));

        if (visit.getInspectionStatus() != InspectionStatus.PENDING) {
            throw new BusinessValidationException("Cannot cancel visit after inspection is completed.");
        }

        visit.setVisitStatus(VisitStatus.CANCELLED);
        return visitMapper.toResponse(visitRepository.save(visit));
    }
}
