package com.farmmarket.buyerservice.exception;

import com.farmmarket.buyerservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.success("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessValidation(BusinessValidationException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ApiResponse<String>> handleIntegrationException(IntegrationException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), HttpStatus.NOT_IMPLEMENTED.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler({ServiceUnavailableException.class, ExternalServiceException.class})
    public ResponseEntity<ApiResponse<String>> handleExternalServiceException(RuntimeException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), HttpStatus.BAD_GATEWAY.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        ApiResponse<String> response = ApiResponse.error("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
