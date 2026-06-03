package com.farmconnect.orderservice.enums;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PICKUP_SCHEDULED,
    VEHICLE_ASSIGNED,
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    LOADING_PENDING,
    LOADED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
