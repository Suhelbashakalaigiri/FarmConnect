package com.farmconnect.logisticsservice.exception;

public class InvalidTrackingStateException extends RuntimeException {
    public InvalidTrackingStateException(String message) {
        super(message);
    }
}
