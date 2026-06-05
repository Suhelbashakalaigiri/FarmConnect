package com.farmmarket.buyerservice.service.impl;

import com.farmmarket.buyerservice.dto.*;
import com.farmmarket.buyerservice.entity.Buyer;
import com.farmmarket.buyerservice.enums.BuyerStatus;
import com.farmmarket.buyerservice.exception.ResourceAlreadyExistsException;
import com.farmmarket.buyerservice.exception.ResourceNotFoundException;
import com.farmmarket.buyerservice.mapper.BuyerMapper;
import com.farmmarket.buyerservice.repository.BuyerRepository;
import com.farmmarket.buyerservice.service.BuyerService;
import com.farmmarket.buyerservice.exception.IntegrationException;
import com.farmmarket.buyerservice.exception.BusinessValidationException;
import com.farmmarket.buyerservice.feign.BidServiceClient;
import com.farmmarket.buyerservice.feign.CropServiceClient;
import com.farmmarket.buyerservice.feign.VisitServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;
    private final CropServiceClient cropServiceClient;
    private final BidServiceClient bidServiceClient;
    private final VisitServiceClient visitServiceClient;

    @Override
    @Transactional
    public BuyerResponse createInternalProfile(CreateBuyerProfileRequest request) {
        log.info("Creating internal buyer profile for ID: {}", request.id());
        
        if (buyerRepository.existsById(request.id())) {
            throw new ResourceAlreadyExistsException("Buyer with ID " + request.id() + " already exists");
        }
        if (buyerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Buyer with email " + request.email() + " already exists");
        }

        Buyer buyer = Buyer.builder()
                .id(request.id())
                .fullName(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .status(BuyerStatus.ACTIVE)
                .profileCompleted(false)
                .build();

        Buyer savedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toResponse(savedBuyer);
    }

    @Override
    @Transactional
    public BuyerResponse completeProfile(Long buyerId, BuyerProfileUpdateRequest request) {
        log.info("Completing buyer profile for ID: {}", buyerId);
        
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));

        buyer.setCompanyName(request.companyName());
        buyer.setBuyerType(request.buyerType());
        buyer.setAddressLine(request.addressLine());
        buyer.setVillageCity(request.villageCity());
        buyer.setDistrict(request.district());
        buyer.setState(request.state());
        buyer.setPincode(request.pincode());
        buyer.setProfileCompleted(true);

        Buyer updatedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toResponse(updatedBuyer);
    }

    @Override
    public List<BuyerResponse> getAllBuyers() {
        return buyerRepository.findAll().stream()
                .map(buyerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BuyerResponse getBuyerById(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
        return buyerMapper.toResponse(buyer);
    }

    @Override
    @Transactional
    public BuyerResponse updateBuyer(Long buyerId, BuyerRequest request) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
        
        buyerMapper.updateBuyerFromRequest(request, buyer);
        
        // Check if mandatory fields are filled to set profileCompleted
        if (isProfileComplete(buyer)) {
            buyer.setProfileCompleted(true);
        }
        
        Buyer updatedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toResponse(updatedBuyer);
    }

    private boolean isProfileComplete(Buyer buyer) {
        return buyer.getAddressLine() != null && !buyer.getAddressLine().isBlank() &&
               buyer.getVillageCity() != null && !buyer.getVillageCity().isBlank() &&
               buyer.getDistrict() != null && !buyer.getDistrict().isBlank() &&
               buyer.getState() != null && !buyer.getState().isBlank() &&
               buyer.getPincode() != null && !buyer.getPincode().isBlank() &&
               buyer.getBuyerType() != null;
    }

    @Override
    @Transactional
    public void deleteBuyer(Long buyerId) {
        if (!buyerRepository.existsById(buyerId)) {
            throw new ResourceNotFoundException("Buyer not found with id: " + buyerId);
        }
        buyerRepository.deleteById(buyerId);
    }

    // --- Real Microservice Communication ---

    @Override
    public List<CropResponse> browseCrops() {
        ApiResponse<List<CropResponse>> response = cropServiceClient.getAllCrops();
        if (response.success() && response.data() != null) {
            return response.data();
        }
        return new ArrayList<>();
    }

    @Override
    public CropResponse getCropById(Long cropId) {
        ApiResponse<CropResponse> response = cropServiceClient.getCropById(cropId);
        if (response.success() && response.data() != null) {
            return response.data();
        }
        throw new ResourceNotFoundException("Crop not found with id: " + cropId);
    }

    @Override
    public String placeBid(BidRequest request) {
        Buyer buyer = buyerRepository.findById(request.buyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + request.buyerId()));
        
        if (!buyer.isProfileCompleted()) {
            throw new BusinessValidationException("You must complete your profile before placing a bid.");
        }

        // Find crop to get farmerId
        ApiResponse<CropResponse> cropResponse = cropServiceClient.getCropById(request.cropId());
        if (!cropResponse.success() || cropResponse.data() == null) {
            throw new ResourceNotFoundException("Crop not found with id: " + request.cropId());
        }

        BidCreateRequest bidCreateRequest = new BidCreateRequest(
                request.cropId(),
                request.buyerId(),
                cropResponse.data().farmerId(),
                request.bidAmount(),
                "Bid placed via Buyer Service"
        );

        ApiResponse<Object> response = bidServiceClient.createBid(bidCreateRequest);
        return response.message();
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        throw new BusinessValidationException("Order can only be created after inspection approval.");
    }

    @Override
    public String approveVisit(Long visitId) {
        // Find buyer to check profile completion (assuming we have buyerId in some way or it's hardcoded for now in this service's context)
        // For simplicity, since the current API doesn't pass buyerId, I'll assume standard validation is enough for now or I'll just add a placeholder comment.
        // Actually, let's keep it consistent.
        ApiResponse<Object> response = visitServiceClient.approveVisit(visitId);
        return response.message();
    }

    @Override
    public String rejectVisit(Long visitId) {
        ApiResponse<Object> response = visitServiceClient.rejectVisit(visitId);
        return response.message();
    }

    @Override
    public DeliveryResponse trackDelivery(String orderId) {
        throw new IntegrationException("Logistics Service not implemented yet.");
    }

    @Override
    public List<TransactionResponse> getTransactionHistory() {
        throw new IntegrationException("Payment Service not implemented yet.");
    }
}
