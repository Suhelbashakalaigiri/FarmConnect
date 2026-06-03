package com.farmconnect.orderservice.dto;

public record ExternalFarmerResponse(
    Long id,
    String fullName,
    String status
) {}
