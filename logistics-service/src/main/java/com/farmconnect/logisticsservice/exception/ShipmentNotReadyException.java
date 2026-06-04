package com.farmconnect.logisticsservice.exception;

public class ShipmentNotReadyException extends RuntimeException {
    public ShipmentNotReadyException(String message) {
        super(message);
    }
}
