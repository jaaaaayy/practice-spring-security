package com.jay.practice_spring_security.controller;

import com.jay.practice_spring_security.dto.auth.LoginDto;
import com.jay.practice_spring_security.dto.auth.RegisterDto;
import com.jay.practice_spring_security.dto.common.ResponseDto;
import com.jay.practice_spring_security.dto.user.UserResponseDto;
import com.jay.practice_spring_security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserResponseDto>> register(@Valid @RequestBody RegisterDto registerDto) {
        UserResponseDto newUser = authService.register(registerDto);
        ResponseDto<UserResponseDto> response = ResponseDto.success("Registered successfully", newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserResponseDto>> login(@Valid @RequestBody LoginDto loginDto) {
        Map<String, Object> loginData = authService.login(loginDto);

        UserResponseDto userData = (UserResponseDto) loginData.get("user");
        String token = (String) loginData.get("token");
        
        ResponseDto<UserResponseDto> response = ResponseDto.success("Login successful", userData, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(value = "Authorization", required = false) String token) {
        return authService.logout(token);
    }
}
