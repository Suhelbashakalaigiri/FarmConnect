package com.farmconnect.logisticsservice.exception;

public class InvalidTrackingTransitionException extends RuntimeException {
    public InvalidTrackingTransitionException(String message) {
        super(message);
    }
}
