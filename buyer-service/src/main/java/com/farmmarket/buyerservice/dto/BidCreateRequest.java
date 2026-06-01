package com.farmmarket.buyerservice.dto;

import java.math.BigDecimal;

public record BidCreateRequest(
    Long cropId,
    Long buyerId,
    Long farmerId,
    BigDecimal bidAmount,
    String remarks
) {}
