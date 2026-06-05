package com.farmmarket.buyerservice.dto;

import com.farmmarket.buyerservice.enums.BuyerStatus;
import com.farmmarket.buyerservice.enums.BuyerType;

import java.time.LocalDateTime;

public record BuyerResponse(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        String companyName,
        String addressLine,
        String villageCity,
        String district,
        String state,
        String pincode,
        boolean profileCompleted,
        BuyerType buyerType,
        BuyerStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
