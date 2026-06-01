package com.farmmarket.farmerservice.dto;

import com.farmmarket.farmerservice.enums.FarmingType;
import com.farmmarket.farmerservice.enums.Gender;
import com.farmmarket.farmerservice.enums.LandUnit;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateFarmerRequest(
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number format")
    String phoneNumber,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    String password,

    @NotNull(message = "Gender is required")
    Gender gender,

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth cannot be in the future")
    LocalDate dateOfBirth,

    @NotBlank(message = "Address line is required")
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
    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be exactly 6 digits")
    String pincode,

    @NotNull(message = "Land area is required")
    @Positive(message = "Land area must be a positive value")
    Double landArea,

    @NotNull(message = "Land unit is required")
    LandUnit landUnit,

    @NotNull(message = "Farming type is required")
    FarmingType farmingType,

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be exactly 12 digits")
    String aadhaarNumber,

    @NotBlank(message = "Bank account number is required")
    @Pattern(regexp = "^\\d{9,18}$", message = "Bank account number must be numeric and between 9 to 18 digits")
    String bankAccountNumber,

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC format")
    String ifscCode,

    String profileImageUrl
) {}
