package com.farmconnect.visitservice.dto;

public record ExternalCropResponse(
    Long id,
    String cropName,
    String status
) {}
