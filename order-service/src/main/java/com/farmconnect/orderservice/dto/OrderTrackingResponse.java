package com.farmconnect.orderservice.dto;

import com.farmconnect.orderservice.enums.OrderStatus;
import java.time.LocalDateTime;

public record OrderTrackingResponse(
    String orderNumber,
    OrderStatus orderStatus,
    LocalDateTime updatedAt,
    String remarks
) {}
