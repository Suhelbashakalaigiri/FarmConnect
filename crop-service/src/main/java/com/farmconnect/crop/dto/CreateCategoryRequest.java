package com.farmconnect.crop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message="Category name is required")
        @Size(max=100)
        String categoryName,

        String description
) {
}
