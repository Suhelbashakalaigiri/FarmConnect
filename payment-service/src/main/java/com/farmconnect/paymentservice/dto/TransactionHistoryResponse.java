package com.farmconnect.paymentservice.dto;

import com.farmconnect.paymentservice.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryResponse(
    String transactionReference,
    Long orderId,
    BigDecimal amount,
    String paymentMethod,
    PaymentStatus paymentStatus,
    LocalDateTime paymentDate
) {}
