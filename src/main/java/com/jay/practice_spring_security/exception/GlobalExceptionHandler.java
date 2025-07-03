package com.jay.practice_spring_security.exception;

import com.jay.practice_spring_security.dto.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleDuplicateResourceException(DuplicateResourceException duplicateResourceException) {
        ResponseDto<Map<String, String>> error = ResponseDto.error("Validation failed");
        error.setErrors(duplicateResourceException.getErrors());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
} 