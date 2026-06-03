package com.farmconnect.orderservice.dto;

import java.math.BigDecimal;

public record ExternalBidResponse(
    Long id,
    String bidStatus,
    BigDecimal bidAmount,
    Long cropId,
    Long buyerId,
    Long farmerId
) {}
