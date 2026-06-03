package com.farmconnect.logisticsservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateLogisticsRequest(
    @NotNull(message = "Order ID is required")
    Long orderId,
    
    @NotNull(message = "Pickup date is required")
    @Future(message = "Pickup date must be in the future")
    LocalDateTime pickupDate,
    
    String remarks
) {}
