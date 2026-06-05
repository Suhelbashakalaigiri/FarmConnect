package com.farmmarket.buyerservice.dto;

import com.farmmarket.buyerservice.enums.BuyerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BuyerProfileUpdateRequest(
    String companyName,

    @NotNull(message = "Buyer type is required")
    BuyerType buyerType,

    @NotBlank(message = "Address is required")
    String addressLine,

    @NotBlank(message = "Village/City is required")
    String villageCity,

    @NotBlank(message = "District is required")
    String district,

    @NotBlank(message = "State is required")
    String state,

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    String pincode
) {}
