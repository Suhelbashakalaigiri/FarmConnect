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
import com.farmmarket.buyerservice.feign.BidServiceClient;
import com.farmmarket.buyerservice.feign.CropServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;
    private final CropServiceClient cropServiceClient;
    private final BidServiceClient bidServiceClient;

    @Override
    @Transactional
    public BuyerResponse registerBuyer(BuyerRequest request) {
        if (buyerRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Buyer with email " + request.email() + " already exists");
        }
        Buyer buyer = buyerMapper.toEntity(request);
        buyer.setStatus(BuyerStatus.ACTIVE);
        Buyer savedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toResponse(savedBuyer);
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
        Buyer updatedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toResponse(updatedBuyer);
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
        throw new IntegrationException("Order Service not implemented yet.");
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
