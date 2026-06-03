package com.farmconnect.logisticsservice.dto;

import com.farmconnect.logisticsservice.enums.TrackingStatus;
import java.time.LocalDateTime;

public record LogisticsTrackingResponse(
    Long orderId,
    TrackingStatus trackingStatus,
    LocalDateTime updatedAt,
    String remarks,
    String driverName,
    String driverPhone,
    String vehicleNumber
) {}
