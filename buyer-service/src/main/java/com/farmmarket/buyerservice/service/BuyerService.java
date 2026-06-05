package com.farmmarket.buyerservice.service;

import com.farmmarket.buyerservice.dto.*;

import java.util.List;

public interface BuyerService {
    BuyerResponse createInternalProfile(CreateBuyerProfileRequest request);
    BuyerResponse completeProfile(Long buyerId, BuyerProfileUpdateRequest request);
    
    List<BuyerResponse> getAllBuyers();
    BuyerResponse getBuyerById(Long buyerId);
    BuyerResponse updateBuyer(Long buyerId, BuyerRequest request);
    void deleteBuyer(Long buyerId);

    List<CropResponse> browseCrops();
    CropResponse getCropById(Long cropId);
    String placeBid(BidRequest request);
    OrderResponse placeOrder(OrderRequest request);

    String approveVisit(Long visitId);
    String rejectVisit(Long visitId);

    DeliveryResponse trackDelivery(String orderId);
    List<TransactionResponse> getTransactionHistory();
}
