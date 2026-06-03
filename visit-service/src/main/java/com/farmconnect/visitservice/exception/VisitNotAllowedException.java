package com.farmconnect.visitservice.exception;

public class VisitNotAllowedException extends RuntimeException {
    public VisitNotAllowedException(String message) {
        super(message);
    }
}
