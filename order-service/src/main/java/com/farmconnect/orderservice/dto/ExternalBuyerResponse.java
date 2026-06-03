package com.farmconnect.orderservice.dto;

public record ExternalBuyerResponse(
    Long buyerId,
    String fullName,
    String status
) {}
