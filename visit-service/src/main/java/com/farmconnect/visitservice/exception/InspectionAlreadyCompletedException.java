package com.farmconnect.visitservice.exception;

public class InspectionAlreadyCompletedException extends RuntimeException {
    public InspectionAlreadyCompletedException(String message) {
        super(message);
    }
}
