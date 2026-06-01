package com.farmmarket.farmerservice.dto;

import com.farmmarket.farmerservice.enums.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CropSummary(
    Long id,
    String cropName,
    Long farmerId,
    Double quantity,
    Double price,
    LocalDate harvestDate,
    String imageUrl,
    Status status,
    String categoryName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
