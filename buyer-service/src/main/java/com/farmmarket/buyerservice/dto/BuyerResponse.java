package com.farmmarket.buyerservice.dto;

import com.farmmarket.buyerservice.enums.BuyerStatus;
import com.farmmarket.buyerservice.enums.BuyerType;

import java.time.LocalDateTime;

public record BuyerResponse(
        Long buyerId,
        String fullName,
        String email,
        String phoneNumber,
        String companyName,
        String addressLine,
        String villageCity,
        String district,
        String state,
        String pincode,
        BuyerType buyerType,
        BuyerStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
