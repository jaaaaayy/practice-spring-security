package com.jay.practice_spring_security.exception;

import com.jay.practice_spring_security.dto.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto<String>> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        ResponseDto<String> response = ResponseDto.error("Incorrect username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        ResponseDto<String> response = ResponseDto.error("Incorrect username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseDto<String>> handleInvalidTokenException(InvalidTokenException invalidTokenException) {
        ResponseDto<String> response = ResponseDto.error(invalidTokenException.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}