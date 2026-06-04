package com.farmconnect.logisticsservice.exception;

public class DeliveryNotAllowedException extends RuntimeException {
    public DeliveryNotAllowedException(String message) {
        super(message);
    }
}
