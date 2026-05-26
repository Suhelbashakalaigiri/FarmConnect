package com.farmconnect.crop.dto;

import com.farmconnect.crop.enums.Status;

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
        Status status,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
