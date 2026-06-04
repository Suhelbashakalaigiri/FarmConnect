package com.farmconnect.paymentservice.feign.dto;

import java.math.BigDecimal;

public record OrderServiceResponse(
    Long id,
    String orderStatus,
    BigDecimal totalAmount
) {}
