package com.farmconnect.crop.dto;

import java.time.LocalDateTime;

public record ExternalFarmerResponse(
    Long id,
    String fullName,
    String status,
    String verificationStatus,
    String availabilityStatus,
    boolean profileCompleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
