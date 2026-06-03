package com.farmconnect.logisticsservice.feign.dto;

public record OrderStatusUpdateRequest(
    String orderStatus,
    String remarks
) {}
