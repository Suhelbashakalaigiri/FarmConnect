package com.farmconnect.crop.exception;

import com.farmconnect.crop.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        ApiResponse<Map<String, String>> response = ApiResponse.success("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessValidationException(BusinessValidationException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleServiceUnavailableException(ServiceUnavailableException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ExternalServiceException.class, IntegrationException.class})
    public ResponseEntity<ApiResponse<Void>> handleIntegrationExceptions(RuntimeException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.BAD_GATEWAY.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
