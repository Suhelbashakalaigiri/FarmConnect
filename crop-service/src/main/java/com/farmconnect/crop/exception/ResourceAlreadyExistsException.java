package com.farmconnect.crop.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(@NotBlank(message = "Category name is required") @Size(max = 100) String s) {
        super(s);
    }
}
