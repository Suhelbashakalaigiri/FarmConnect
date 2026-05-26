package com.farmmarket.buyerservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        String orderId,
        BigDecimal amount,
        String paymentMethod,
        String status,
        LocalDateTime transactionDate
) {
}
