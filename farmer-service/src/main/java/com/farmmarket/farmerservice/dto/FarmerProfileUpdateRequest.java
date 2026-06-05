package com.farmmarket.farmerservice.dto;

import com.farmmarket.farmerservice.enums.FarmingType;
import com.farmmarket.farmerservice.enums.LandUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record FarmerProfileUpdateRequest(
    @NotNull(message = "Land area is required")
    @Positive(message = "Land area must be positive")
    Double landArea,

    @NotNull(message = "Land unit is required")
    LandUnit landUnit,

    @NotNull(message = "Farming type is required")
    FarmingType farmingType,

    @NotBlank(message = "Address is required")
    String addressLine,

    @NotBlank(message = "Village is required")
    String village,

    @NotBlank(message = "Mandal is required")
    String mandal,

    @NotBlank(message = "District is required")
    String district,

    @NotBlank(message = "State is required")
    String state,

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    String pincode
) {}
