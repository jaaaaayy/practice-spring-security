package com.jay.practice_spring_security.controller;

import com.jay.practice_spring_security.model.User;
import com.jay.practice_spring_security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(value = "Authorization", required = false) String token) {
        return authService.logout(token);
    }
}
