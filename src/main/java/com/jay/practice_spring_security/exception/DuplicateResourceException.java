package com.jay.practice_spring_security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DuplicateResourceException extends RuntimeException {
    private Map<String, String> errors;
}
