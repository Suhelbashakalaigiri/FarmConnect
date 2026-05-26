package com.farmmarket.buyerservice.service;

import com.farmmarket.buyerservice.dto.*;

import java.util.List;

public interface BuyerService {
    BuyerResponse registerBuyer(BuyerRequest request);
    List<BuyerResponse> getAllBuyers();
    BuyerResponse getBuyerById(Long buyerId);
    BuyerResponse updateBuyer(Long buyerId, BuyerRequest request);
    void deleteBuyer(Long buyerId);

    // Mock Methods
    List<CropResponse> browseCrops();
    CropResponse getCropById(Long cropId);
    String placeBid(BidRequest request);
    OrderResponse placeOrder(OrderRequest request);
    DeliveryResponse trackDelivery(String orderId);
    List<TransactionResponse> getTransactionHistory();
}
