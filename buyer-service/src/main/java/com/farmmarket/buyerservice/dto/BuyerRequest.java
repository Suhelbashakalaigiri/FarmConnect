package com.farmmarket.buyerservice.dto;

import com.farmmarket.buyerservice.enums.BuyerType;
import jakarta.validation.constraints.*;

public record BuyerRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        String companyName,

        @NotBlank(message = "Address line is required")
        String addressLine,

        @NotBlank(message = "Village/City is required")
        String villageCity,

        @NotBlank(message = "District is required")
        String district,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Pincode is required")
        @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
        String pincode,

        @NotNull(message = "Buyer type is required")
        BuyerType buyerType
) {
}
