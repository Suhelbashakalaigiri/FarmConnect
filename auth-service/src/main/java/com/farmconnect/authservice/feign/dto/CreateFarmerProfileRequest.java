package com.farmconnect.authservice.feign.dto;

public record CreateFarmerProfileRequest(
    Long id,
    String fullName,
    String email,
    String phoneNumber
) {}
