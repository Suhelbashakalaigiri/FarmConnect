package com.farmconnect.visitservice.dto;

import jakarta.validation.constraints.NotBlank;

public record InspectionApprovalRequest(
    @NotBlank(message = "Remarks are required for approval")
    String remarks
) {}
