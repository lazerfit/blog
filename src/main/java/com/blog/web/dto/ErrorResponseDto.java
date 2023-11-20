package com.blog.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter

public class ErrorResponseDto {

    private final String code;
    private final String message;
    private final List<ValidationError> validationErrors = new ArrayList<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validationErrors.add(new ValidationError(fieldName, errorMessage));
    }

    @Builder
    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private record ValidationError(String fieldName, String errorMessage) {}
}
