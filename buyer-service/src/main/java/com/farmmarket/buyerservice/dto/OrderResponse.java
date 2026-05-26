package com.farmmarket.buyerservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        String orderId,
        String cropName,
        Double quantity,
        BigDecimal totalAmount,
        String status,
        LocalDateTime orderDate
) {
}
