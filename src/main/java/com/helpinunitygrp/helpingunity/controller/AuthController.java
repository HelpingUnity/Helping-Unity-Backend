package com.helpinunitygrp.helpingunity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpinunitygrp.helpingunity.payload.LoginRequest;
import com.helpinunitygrp.helpingunity.payload.RegisterRequest;
import com.helpinunitygrp.helpingunity.repository.Userrepository;
import com.helpinunitygrp.helpingunity.security.JwtTokenProvider;
import com.helpinunitygrp.helpingunity.service.AuthService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private Userrepository userrepository;
    
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    

}
