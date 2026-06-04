package com.farmconnect.paymentservice.feign.dto;

public record LogisticsStatusUpdateRequest(
    String trackingStatus,
    String remarks
) {}
