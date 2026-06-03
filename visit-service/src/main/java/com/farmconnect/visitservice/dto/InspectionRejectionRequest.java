package com.farmconnect.visitservice.dto;

import jakarta.validation.constraints.NotBlank;

public record InspectionRejectionRequest(
    @NotBlank(message = "Remarks are required for rejection")
    String remarks
) {}
