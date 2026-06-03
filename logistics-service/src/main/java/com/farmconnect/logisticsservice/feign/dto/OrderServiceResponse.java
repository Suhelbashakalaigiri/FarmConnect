package com.farmconnect.logisticsservice.feign.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderServiceResponse(
    Long id,
    String orderNumber,
    Long cropId,
    Long buyerId,
    Long farmerId,
    Double quantity,
    BigDecimal totalAmount,
    String orderStatus,
    LocalDateTime createdAt
) {}
