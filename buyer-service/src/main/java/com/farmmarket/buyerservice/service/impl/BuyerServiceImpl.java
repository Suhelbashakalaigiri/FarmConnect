package com.farmmarket.buyerservice.service.impl;

import com.farmmarket.buyerservice.dto.*;
import com.farmmarket.buyerservice.entity.Buyer;
import com.farmmarket.buyerservice.enums.BuyerStatus;
import com.farmmarket.buyerservice.exception.ResourceAlreadyExistsException;
import com.farmmarket.buyerservice.exception.ResourceNotFoundException;
import com.farmmarket.buyerservice.mapper.BuyerMapper;
import com.farmmarket.buyerservice.repository.BuyerRepository;
import com.farmmarket.buyerservice.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;

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

    // --- Mock Implementations ---

    @Override
    public List<CropResponse> browseCrops() {
        List<CropResponse> crops = new ArrayList<>();
        crops.add(new CropResponse(1L, "Premium Wheat", "Grains", new BigDecimal("25.50"), 5000.0, "John Doe", "Punjab"));
        crops.add(new CropResponse(2L, "Organic Basmati Rice", "Grains", new BigDecimal("85.00"), 2000.0, "Sukhwinder Singh", "Haryana"));
        crops.add(new CropResponse(3L, "Red Onions", "Vegetables", new BigDecimal("30.00"), 10000.0, "Rajesh Kumar", "Maharashtra"));
        return crops;
    }

    @Override
    public CropResponse getCropById(Long cropId) {
        return new CropResponse(cropId, "Mock Crop " + cropId, "Mock Category", new BigDecimal("50.00"), 100.0, "Mock Farmer", "Mock Location");
    }

    @Override
    public String placeBid(BidRequest request) {
        return "Bid of " + request.bidAmount() + " placed successfully for Crop ID: " + request.cropId();
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        return new OrderResponse(
                UUID.randomUUID().toString(),
                "Mock Crop " + request.cropId(),
                request.quantity(),
                new BigDecimal("5000.00"),
                "CONFIRMED",
                LocalDateTime.now()
        );
    }

    @Override
    public DeliveryResponse trackDelivery(String orderId) {
        return new DeliveryResponse(
                "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                orderId,
                "IN_TRANSIT",
                "2026-06-01",
                "Delhi Hub",
                LocalDateTime.now()
        );
    }

    @Override
    public List<TransactionResponse> getTransactionHistory() {
        List<TransactionResponse> transactions = new ArrayList<>();
        transactions.add(new TransactionResponse(
                "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString(),
                new BigDecimal("12000.00"),
                "UPI",
                "SUCCESS",
                LocalDateTime.now().minusDays(2)
        ));
        return transactions;
    }
}
