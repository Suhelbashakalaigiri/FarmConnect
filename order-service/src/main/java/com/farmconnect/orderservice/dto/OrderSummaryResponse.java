package com.farmconnect.orderservice.dto;

import com.farmconnect.orderservice.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
    Long id,
    String orderNumber,
    Long cropId,
    Double quantity,
    BigDecimal totalAmount,
    OrderStatus orderStatus,
    LocalDateTime createdAt
) {}
