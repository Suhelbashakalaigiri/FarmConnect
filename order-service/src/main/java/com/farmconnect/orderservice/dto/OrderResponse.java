package com.farmconnect.orderservice.dto;

import com.farmconnect.orderservice.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    String orderNumber,
    Long visitId,
    Long bidId,
    Long cropId,
    Long buyerId,
    Long farmerId,
    Double quantity,
    BigDecimal unitPrice,
    BigDecimal totalAmount,
    OrderStatus orderStatus,
    String remarks,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
