package com.helpinunitygrp.helpingunity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.*;
import org.springframework.stereotype.Service;

import com.helpinunitygrp.helpingunity.model.User;
import com.helpinunitygrp.helpingunity.payload.JwtResponse;
import com.helpinunitygrp.helpingunity.payload.LoginRequest;
import com.helpinunitygrp.helpingunity.payload.RegisterRequest;
import com.helpinunitygrp.helpingunity.repository.Userrepository;
import com.helpinunitygrp.helpingunity.security.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
public class AuthService {
    
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final Userrepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    
    public AuthService(AuthenticationConfiguration authenticationConfiguration,
                      JwtTokenProvider jwtTokenProvider,
                      Userrepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        try {
            this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize AuthenticationManager", e);
        }
    }
    
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully");
    }
}
