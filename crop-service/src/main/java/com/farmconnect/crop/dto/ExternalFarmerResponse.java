package com.farmconnect.crop.dto;

import java.time.LocalDateTime;

public record ExternalFarmerResponse(
    Long id,
    String fullName,
    String status,
    String verificationStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
