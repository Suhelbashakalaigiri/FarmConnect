package com.farmconnect.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotNull(message = "Order ID is required")
    Long orderId,
    
    @NotNull(message = "Buyer ID is required")
    Long buyerId,
    
    @NotNull(message = "Farmer ID is required")
    Long farmerId,
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal amount,
    
    @NotNull(message = "Currency is required")
    String currency
) {}
