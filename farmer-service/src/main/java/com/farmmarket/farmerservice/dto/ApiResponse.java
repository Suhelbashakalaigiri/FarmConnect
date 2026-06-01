package com.farmmarket.farmerservice.dto;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        boolean success,
        String message,
        int statusCode,
        T data,
        LocalDateTime timestamp
) {
    public ApiResponse(boolean success, String message, int statusCode, T data) {
        this(success, message, statusCode, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, int statusCode, T data) {
        return new ApiResponse<>(true, message, statusCode, data);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, message, statusCode, null);
    }
}
