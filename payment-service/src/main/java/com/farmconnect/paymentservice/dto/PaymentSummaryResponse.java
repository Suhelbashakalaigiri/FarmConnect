package com.farmconnect.paymentservice.dto;

import com.farmconnect.paymentservice.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSummaryResponse(
    Long id,
    Long orderId,
    BigDecimal amount,
    PaymentStatus paymentStatus,
    LocalDateTime paymentDate
) {}
