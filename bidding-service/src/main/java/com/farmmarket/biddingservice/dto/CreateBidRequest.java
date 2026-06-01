package com.farmmarket.biddingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateBidRequest(
        @NotNull(message = "Crop ID is required")
        Long cropId,
        @NotNull(message = "Buyer ID is required")
        Long buyerId,
        @NotNull(message = "Farmer ID is required")
        Long farmerId,
        @NotNull(message = "Bid amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Bid amount must be greater than 0")
        BigDecimal bidAmount,
        String remarks
) {}
