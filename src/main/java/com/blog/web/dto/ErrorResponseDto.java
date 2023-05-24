package com.blog.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final String code;
    private final String message;
    private final List<ValidationError> validationErrors = new ArrayList<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validationErrors.add(new ValidationError(fieldName, errorMessage));
    }


    private record ValidationError(String fieldName, String errorMessage) {}
}
