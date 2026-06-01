package com.farmmarket.farmerservice.dto;

import com.farmmarket.farmerservice.enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FarmerResponse(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    Gender gender,
    LocalDate dateOfBirth,
    String addressLine,
    String village,
    String mandal,
    String district,
    String state,
    String pincode,
    Double landArea,
    LandUnit landUnit,
    FarmingType farmingType,
    String aadhaarNumber,
    String bankAccountNumber,
    String ifscCode,
    String profileImageUrl,
    FarmerStatus status,
    VerificationStatus verificationStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
