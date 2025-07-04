package com.jay.practice_spring_security.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private String message;
    private T data;
    private String token;
    private T errors;

    public ResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResponseDto(String message, T data, String token) {
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(T data) {
        this.data = data;
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(message, data);
    }

    public static <T> ResponseDto<T> success(String message, T data, String token) {
        return new ResponseDto<>(message, data, token);
    }

    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(message);
    }
}
