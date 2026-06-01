package com.farmmarket.biddingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HighestBidResponse(
        Long id,
        Long cropId,
        Long buyerId,
        BigDecimal bidAmount,
        LocalDateTime bidTime
) {}
