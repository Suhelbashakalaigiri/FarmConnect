package com.farmconnect.logisticsservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AssignVehicleRequest(
    @NotBlank(message = "Driver name is required")
    String driverName,
    
    @NotBlank(message = "Driver phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Driver phone must be 10 digits")
    String driverPhone,
    
    @NotBlank(message = "Vehicle number is required")
    String vehicleNumber,
    
    @NotBlank(message = "Vehicle type is required")
    String vehicleType
) {}
