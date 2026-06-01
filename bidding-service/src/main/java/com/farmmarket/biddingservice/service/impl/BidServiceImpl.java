package com.farmmarket.biddingservice.service.impl;

import com.farmmarket.biddingservice.dto.*;
import com.farmmarket.biddingservice.entity.Bid;
import com.farmmarket.biddingservice.enums.BidStatus;
import com.farmmarket.biddingservice.exception.*;
import com.farmmarket.biddingservice.feign.BuyerServiceClient;
import com.farmmarket.biddingservice.feign.CropServiceClient;
import com.farmmarket.biddingservice.feign.FarmerServiceClient;
import com.farmmarket.biddingservice.mapper.BidMapper;
import com.farmmarket.biddingservice.repository.BidRepository;
import com.farmmarket.biddingservice.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final BidMapper bidMapper;
    private final CropServiceClient cropServiceClient;
    private final BuyerServiceClient buyerServiceClient;
    private final FarmerServiceClient farmerServiceClient;

    @Override
    @Transactional
    public BidResponse createBid(CreateBidRequest request) {
        log.info("Creating bid for crop: {} by buyer: {}", request.cropId(), request.buyerId());

        // 1. Validate Crop
        ApiResponse<ExternalCropResponse> cropResponse = cropServiceClient.getCropById(request.cropId());
        if (!cropResponse.success() || cropResponse.data() == null) {
            throw new ResourceNotFoundException("Crop not found with ID: " + request.cropId());
        }
        ExternalCropResponse crop = cropResponse.data();
        if (!"AVAILABLE".equalsIgnoreCase(crop.status())) {
            throw new BidNotAllowedException("Bidding not allowed. Crop status is: " + crop.status());
        }

        // 2. Validate Buyer
        ApiResponse<ExternalBuyerResponse> buyerResponse = buyerServiceClient.getBuyerById(request.buyerId());
        if (!buyerResponse.success() || buyerResponse.data() == null) {
            throw new ResourceNotFoundException("Buyer not found with ID: " + request.buyerId());
        }
        if (!"ACTIVE".equalsIgnoreCase(buyerResponse.data().status())) {
            throw new BusinessValidationException("Only ACTIVE buyers can place bids.");
        }

        // 3. Validate Farmer
        ApiResponse<ExternalFarmerResponse> farmerResponse = farmerServiceClient.getFarmerById(request.farmerId());
        if (!farmerResponse.success() || farmerResponse.data() == null) {
            throw new ResourceNotFoundException("Farmer not found with ID: " + request.farmerId());
        }
        if (!"ACTIVE".equalsIgnoreCase(farmerResponse.data().status())) {
            throw new BusinessValidationException("Farmer status is not ACTIVE.");
        }

        // RULE 9: No bids allowed after a bid is accepted
        if (bidRepository.existsByCropIdAndBidStatus(request.cropId(), BidStatus.ACCEPTED)) {
            throw new BidNotAllowedException("A bid has already been accepted for this crop.");
        }

        // RULE 1: Bid amount must be greater than crop base price
        if (request.bidAmount().compareTo(java.math.BigDecimal.valueOf(crop.price())) <= 0) {
            throw new BusinessValidationException("Bid amount must be greater than crop base price: " + crop.price());
        }

        // RULE 2: Bid amount must be greater than current highest bid
        Optional<Bid> highestBidOpt = bidRepository.findHighestBidByCropId(request.cropId());
        if (highestBidOpt.isPresent()) {
            Bid currentHighest = highestBidOpt.get();
            if (request.bidAmount().compareTo(currentHighest.getBidAmount()) <= 0) {
                throw new BusinessValidationException("Bid amount must be greater than current highest bid: " + currentHighest.getBidAmount());
            }
            // RULE 5: Previous highest bid -> OUTBID
            currentHighest.setBidStatus(BidStatus.OUTBID);
            bidRepository.save(currentHighest);
        }

        Bid bid = Bid.builder()
                .cropId(request.cropId())
                .buyerId(request.buyerId())
                .farmerId(request.farmerId())
                .bidAmount(request.bidAmount())
                .bidStatus(BidStatus.ACTIVE)
                .remarks(request.remarks())
                .bidTime(LocalDateTime.now())
                .build();

        Bid savedBid = bidRepository.save(bid);
        return bidMapper.toResponse(savedBid);
    }

    @Override
    public BidResponse getBidById(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + id));
        return bidMapper.toResponse(bid);
    }

    @Override
    public List<BidResponse> getAllBids() {
        return bidMapper.toResponseList(bidRepository.findAll());
    }

    @Override
    public List<BidResponse> getBidsByCrop(Long cropId) {
        return bidMapper.toResponseList(bidRepository.findByCropId(cropId));
    }

    @Override
    public List<BidResponse> getBidsByBuyer(Long buyerId) {
        return bidMapper.toResponseList(bidRepository.findByBuyerId(buyerId));
    }

    @Override
    public HighestBidResponse getHighestBid(Long cropId) {
        Bid highestBid = bidRepository.findHighestBidByCropId(cropId)
                .orElseThrow(() -> new ResourceNotFoundException("No active bids found for crop ID: " + cropId));
        return bidMapper.toHighestBidResponse(highestBid);
    }

    @Override
    @Transactional
    public BidResponse acceptBid(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + id));

        if (bid.getBidStatus() == BidStatus.ACCEPTED) {
            throw new BidAlreadyAcceptedException("Bid is already accepted.");
        }
        
        if (bid.getBidStatus() == BidStatus.REJECTED || bid.getBidStatus() == BidStatus.WITHDRAWN) {
            throw new BusinessValidationException("Cannot accept a " + bid.getBidStatus() + " bid.");
        }

        // RULE 6: Selected Bid -> ACCEPTED, All remaining bids of same crop -> REJECTED
        bid.setBidStatus(BidStatus.ACCEPTED);
        
        List<Bid> otherBids = bidRepository.findByCropId(bid.getCropId());
        for (Bid b : otherBids) {
            if (!b.getId().equals(bid.getId()) && (b.getBidStatus() == BidStatus.ACTIVE || b.getBidStatus() == BidStatus.OUTBID)) {
                b.setBidStatus(BidStatus.REJECTED);
                bidRepository.save(b);
            }
        }

        return bidMapper.toResponse(bidRepository.save(bid));
    }

    @Override
    @Transactional
    public BidResponse rejectBid(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + id));

        if (bid.getBidStatus() == BidStatus.ACCEPTED) {
            throw new BidAlreadyAcceptedException("Cannot reject an already accepted bid.");
        }

        bid.setBidStatus(BidStatus.REJECTED);
        return bidMapper.toResponse(bidRepository.save(bid));
    }

    @Override
    @Transactional
    public BidResponse withdrawBid(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with ID: " + id));

        if (bid.getBidStatus() == BidStatus.ACCEPTED) {
            throw new BidAlreadyAcceptedException("Cannot withdraw an already accepted bid.");
        }

        bid.setBidStatus(BidStatus.WITHDRAWN);
        return bidMapper.toResponse(bidRepository.save(bid));
    }
}
