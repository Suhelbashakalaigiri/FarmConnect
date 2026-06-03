package com.farmconnect.logisticsservice.dto;

import com.farmconnect.logisticsservice.enums.TrackingStatus;
import java.time.LocalDateTime;

public record LogisticsResponse(
    Long id,
    Long orderId,
    LocalDateTime pickupDate,
    LocalDateTime vehicleAssignedDate,
    LocalDateTime arrivalDate,
    LocalDateTime shipmentDate,
    LocalDateTime deliveryDate,
    String driverName,
    String driverPhone,
    String vehicleNumber,
    String vehicleType,
    TrackingStatus trackingStatus,
    String remarks,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
