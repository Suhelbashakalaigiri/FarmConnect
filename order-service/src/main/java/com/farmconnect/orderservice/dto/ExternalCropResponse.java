package com.farmconnect.orderservice.dto;

public record ExternalCropResponse(
    Long id,
    String cropName,
    Double quantity,
    Double price,
    String status
) {}
