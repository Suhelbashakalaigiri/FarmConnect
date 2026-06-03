package com.farmconnect.logisticsservice.dto;

import com.farmconnect.logisticsservice.enums.TrackingStatus;
import java.time.LocalDateTime;

public record LogisticsSummaryResponse(
    Long id,
    Long orderId,
    TrackingStatus trackingStatus,
    LocalDateTime pickupDate,
    String vehicleNumber
) {}
