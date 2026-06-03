package com.farmconnect.visitservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RescheduleVisitRequest(
    @NotNull(message = "Visit ID is required")
    Long visitId,
    
    @NotNull(message = "New visit date is required")
    @Future(message = "New visit date must be in the future")
    LocalDateTime newVisitDate
) {}
