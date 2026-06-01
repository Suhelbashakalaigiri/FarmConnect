package com.farmmarket.buyerservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CropResponse(
        Long id,
        String cropName,
        Long farmerId,
        Double quantity,
        Double price,
        LocalDate harvestDate,
        String imageUrl,
        String status,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
