package com.farmconnect.authservice.feign.dto;

public record CreateBuyerProfileRequest(
    Long id,
    String fullName,
    String email,
    String phoneNumber
) {}
