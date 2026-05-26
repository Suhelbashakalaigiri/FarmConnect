package com.farmmarket.buyerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BidRequest(
        @NotNull(message = "Crop ID is required")
        Long cropId,

        @NotNull(message = "Buyer ID is required")
        Long buyerId,

        @NotNull(message = "Bid amount is required")
        @Positive(message = "Bid amount must be positive")
        BigDecimal bidAmount,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Double quantity
) {
}
