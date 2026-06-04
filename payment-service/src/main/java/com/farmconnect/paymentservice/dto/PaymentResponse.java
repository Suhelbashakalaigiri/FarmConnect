package com.farmconnect.paymentservice.dto;

import com.farmconnect.paymentservice.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    Long orderId,
    BigDecimal amount,
    String currency,
    String razorpayOrderId,
    PaymentStatus paymentStatus,
    String transactionReference,
    LocalDateTime createdAt
) {}
