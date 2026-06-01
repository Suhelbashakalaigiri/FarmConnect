package com.farmmarket.biddingservice.dto;

import java.time.LocalDateTime;

public record ExternalBuyerResponse(
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
        String buyerType,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
