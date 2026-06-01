package com.farmmarket.biddingservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExternalFarmerResponse(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    String gender,
    LocalDate dateOfBirth,
    String addressLine,
    String village,
    String mandal,
    String district,
    String state,
    String pincode,
    Double landArea,
    String landUnit,
    String farmingType,
    String aadhaarNumber,
    String bankAccountNumber,
    String ifscCode,
    String profileImageUrl,
    String status,
    String verificationStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
