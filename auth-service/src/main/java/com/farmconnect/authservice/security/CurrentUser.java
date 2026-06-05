package com.farmconnect.authservice.security;

public record CurrentUser(
    Long userId,
    String email,
    String role
) {}
