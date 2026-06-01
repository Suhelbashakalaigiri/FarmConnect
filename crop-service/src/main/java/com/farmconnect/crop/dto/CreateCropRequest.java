package com.farmconnect.crop.dto;

import com.farmconnect.crop.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateCropRequest(
        @NotNull(message= " crop Name is Required")
        @Size(max=100)
        String cropName,
        @NotNull(message="Quantity is required")
        @Positive(message="quantity should be positive")
        Double quantity,
        @NotNull(message="Price is required")
        @Positive(message="Price musr be positive")
        Double price,
        @NotNull(message="Harvest Date Must be provided")
        LocalDate harvestDate,
        @NotNull(message="Status is required")
        Status status,
        @NotNull(message="category name is required")
        String categoryName,
        @NotNull(message="Farmer ID is required")
        Long farmerId
) {
}
