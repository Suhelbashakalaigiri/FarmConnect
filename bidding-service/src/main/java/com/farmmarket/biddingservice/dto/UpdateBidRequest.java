package com.farmmarket.biddingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateBidRequest(
        @NotNull(message = "Bid amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Bid amount must be greater than 0")
        BigDecimal bidAmount,
        String remarks
) {}
