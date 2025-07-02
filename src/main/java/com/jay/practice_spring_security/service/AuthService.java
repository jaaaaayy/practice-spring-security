package com.jay.practice_spring_security.service;

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

    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(User user) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(user);
        }

        return "Login successful";
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
