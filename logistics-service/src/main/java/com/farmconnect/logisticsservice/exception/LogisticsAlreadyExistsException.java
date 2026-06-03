package com.farmconnect.logisticsservice.exception;

public class LogisticsAlreadyExistsException extends RuntimeException {
    public LogisticsAlreadyExistsException(String message) {
        super(message);
    }
}
