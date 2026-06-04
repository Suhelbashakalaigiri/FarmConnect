package com.farmconnect.logisticsservice.exception;

import com.farmconnect.logisticsservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessValidation(BusinessValidationException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LogisticsAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleLogisticsAlreadyExists(LogisticsAlreadyExistsException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTrackingStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTrackingState(InvalidTrackingStateException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTrackingTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTrackingTransition(InvalidTrackingTransitionException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShipmentNotReadyException.class)
    public ResponseEntity<ApiResponse<Void>> handleShipmentNotReady(ShipmentNotReadyException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeliveryNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeliveryNotAllowed(DeliveryNotAllowedException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleAssignmentException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehicleAssignment(VehicleAssignmentException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShipmentTrackingException.class)
    public ResponseEntity<ApiResponse<Void>> handleShipmentTracking(ShipmentTrackingException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(ApiResponse.error("Validation Failed", HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ApiResponse.error("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
