package com.farmconnect.orderservice.dto;

import java.util.List;

public record ApiResponse<T>(
    boolean success,
    String message,
    int statusCode,
    T data,
    List<String> errors
) {
    public static <T> ApiResponse<T> success(String message, int statusCode, T data) {
        return new ApiResponse<>(true, message, statusCode, data, null);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode, List<String> errors) {
        return new ApiResponse<>(false, message, statusCode, null, errors);
    }
}
