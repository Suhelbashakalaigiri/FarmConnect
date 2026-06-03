package com.farmconnect.visitservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateVisitRequest(
    @NotNull(message = "Bid ID is required")
    Long bidId,
    
    @NotNull(message = "Farmer ID is required")
    Long farmerId,
    
    @NotNull(message = "Buyer ID is required")
    Long buyerId,
    
    @NotNull(message = "Crop ID is required")
    Long cropId,
    
    @NotNull(message = "Visit date is required")
    @Future(message = "Visit date must be in the future")
    LocalDateTime visitDate
) {}
