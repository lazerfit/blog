package com.blog.web.controller;

import com.blog.exception.TopException;
import com.blog.web.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(
        MethodArgumentNotValidException ex) {

        int code = HttpStatus.BAD_REQUEST.value();
        String message="잘못된 요청입니다.";

        ErrorResponseDto responseDto=new ErrorResponseDto(String.valueOf(code),message);

        ex.getBindingResult().getFieldErrors().forEach(
            c->responseDto.addValidation(c.getField(),c.getDefaultMessage())
        );

        return responseDto;
    }

    @ExceptionHandler(TopException.class)
    public ResponseEntity<ErrorResponseDto> handlePostNotFoundExceptions(TopException ex) {

        int code = ex.getStatusCode();
        String message=ex.getMessage();

        ErrorResponseDto response = new ErrorResponseDto(String.valueOf(code), message);

        return ResponseEntity.status(code)
            .body(response);
    }
}
