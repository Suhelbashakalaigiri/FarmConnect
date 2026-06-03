package com.farmconnect.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
    @NotNull(message = "Visit ID is required")
    Long visitId,
    
    @NotNull(message = "Bid ID is required")
    Long bidId,
    
    @NotNull(message = "Crop ID is required")
    Long cropId,
    
    @NotNull(message = "Buyer ID is required")
    Long buyerId,
    
    @NotNull(message = "Farmer ID is required")
    Long farmerId,
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    Double quantity,
    
    String remarks
) {}
