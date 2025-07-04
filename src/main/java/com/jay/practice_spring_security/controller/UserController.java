package com.jay.practice_spring_security.controller;

import com.jay.practice_spring_security.dto.common.ResponseDto;
import com.jay.practice_spring_security.dto.user.UserResponseDto;
import com.jay.practice_spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.findAll();

        ResponseDto<List<UserResponseDto>> response = ResponseDto.success(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
