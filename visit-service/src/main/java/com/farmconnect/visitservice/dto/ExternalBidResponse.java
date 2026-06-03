package com.farmconnect.visitservice.dto;

public record ExternalBidResponse(
    Long id,
    String bidStatus,
    Long cropId,
    Long buyerId,
    Long farmerId
) {}
