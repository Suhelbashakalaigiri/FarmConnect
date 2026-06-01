package com.farmmarket.biddingservice.dto;

import com.farmmarket.biddingservice.enums.BidStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidResponse(
        Long id,
        Long cropId,
        Long buyerId,
        Long farmerId,
        BigDecimal bidAmount,
        BidStatus bidStatus,
        String remarks,
        LocalDateTime bidTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
