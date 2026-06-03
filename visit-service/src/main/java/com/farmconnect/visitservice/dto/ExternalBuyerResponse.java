package com.farmconnect.visitservice.dto;

public record ExternalBuyerResponse(
    Long buyerId,
    String fullName,
    String status
) {}
