package com.jay.practice_spring_security.service;

import com.jay.practice_spring_security.CustomUserDetails;
import com.jay.practice_spring_security.dto.auth.LoginDto;
import com.jay.practice_spring_security.dto.auth.RegisterDto;
import com.jay.practice_spring_security.dto.user.UserResponseDto;
import com.jay.practice_spring_security.exception.DuplicateResourceException;
import com.jay.practice_spring_security.model.BlacklistedToken;
import com.jay.practice_spring_security.model.User;
import com.jay.practice_spring_security.repository.BlacklistedTokenRepository;
import com.jay.practice_spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, BlacklistedTokenRepository blacklistedTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public UserResponseDto register(RegisterDto registerDto) {
        Map<String, String> errors = new HashMap<>();

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            errors.put("username", "Username already taken");
        }

        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            errors.put("email", "Email already exists");
        }

        if (!errors.isEmpty()) {
            throw new DuplicateResourceException(errors);
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));

        User newUser = userRepository.save(user);
        return new UserResponseDto(newUser);
    }

    public Map<String, Object> login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        UserResponseDto user = new UserResponseDto(userDetails.getUser());
        String token = jwtService.generateToken(loginDto);

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("user", user);
        loginData.put("token", token);
        
        return loginData;
    }

    public String logout(String token) {
        if (token == null || token.isEmpty()) {
            return "Token is required";
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (jwtService.isTokenBlacklisted(token)) {
            return "Token is already blacklisted";
        }

        Date expirationDate = jwtService.extractExpiration(token);
        Instant expiresAt = expirationDate.toInstant();

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(expiresAt);
        blacklistedTokenRepository.save(blacklistedToken);

        return "Logout sucessful";
    }
}
