package com.jay.practice_spring_security.controller;

import com.jay.practice_spring_security.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping
    public List<User> getAllUsers() {
        return Arrays.asList(
                new User(1L, "jane", "jane@gmail.com", "password", Instant.now(), Instant.now()),
                new User(1L, "peter", "peter@gmail.com", "password", Instant.now(), Instant.now()),
                new User(1L, "michael", "michael@gmail.com", "password", Instant.now(), Instant.now())
        );
    }
}
