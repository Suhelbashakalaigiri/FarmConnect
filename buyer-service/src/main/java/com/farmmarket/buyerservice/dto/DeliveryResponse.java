package com.farmmarket.buyerservice.dto;

import java.time.LocalDateTime;

public record DeliveryResponse(
        String trackingId,
        String orderId,
        String currentStatus,
        String estimatedDelivery,
        String lastLocation,
        LocalDateTime lastUpdate
) {
}
