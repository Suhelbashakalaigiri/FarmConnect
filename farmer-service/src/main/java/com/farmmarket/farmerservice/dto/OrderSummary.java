package com.farmmarket.farmerservice.dto;

public record OrderSummary(
    Long orderId,
    String cropName,
    Double quantity,
    Double totalAmount,
    String buyerName,
    String status
) {}
