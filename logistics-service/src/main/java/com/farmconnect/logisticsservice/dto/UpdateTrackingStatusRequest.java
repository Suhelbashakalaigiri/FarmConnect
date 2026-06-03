package com.farmconnect.logisticsservice.dto;

import com.farmconnect.logisticsservice.enums.TrackingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTrackingStatusRequest(
    @NotNull(message = "Tracking status is required")
    TrackingStatus trackingStatus,
    String remarks
) {}
