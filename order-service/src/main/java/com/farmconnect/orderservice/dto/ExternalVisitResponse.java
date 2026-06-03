package com.farmconnect.orderservice.dto;

public record ExternalVisitResponse(
    Long id,
    Long bidId,
    Long cropId,
    Long buyerId,
    Long farmerId,
    String visitStatus,
    String inspectionStatus
) {}
