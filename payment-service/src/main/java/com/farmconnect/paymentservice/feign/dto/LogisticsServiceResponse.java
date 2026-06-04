package com.farmconnect.paymentservice.feign.dto;

public record LogisticsServiceResponse(
    Long id,
    String trackingStatus
) {}
