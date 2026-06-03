package com.farmconnect.orderservice.dto;

public record ExternalBuyerResponse(
    Long id,
    String fullName,
    String status
) {}
