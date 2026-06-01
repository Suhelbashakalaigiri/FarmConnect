package com.farmmarket.biddingservice.dto;

import com.farmmarket.biddingservice.enums.BidStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidSummaryResponse(
        Long id,
        Long cropId,
        BigDecimal bidAmount,
        BidStatus bidStatus,
        LocalDateTime bidTime
) {}
