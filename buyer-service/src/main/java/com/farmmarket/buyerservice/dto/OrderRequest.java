package com.farmmarket.buyerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @NotNull(message = "Crop ID is required")
        Long cropId,

        @NotNull(message = "Buyer ID is required")
        Long buyerId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Double quantity
) {
}
