package com.helpinunitygrp.helpingunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpinunitygrp.helpingunity.model.User;
import com.helpinunitygrp.helpingunity.repository.Userrepository;
import com.helpinunitygrp.helpingunity.security.JwtTokenProvider;

@Service
public class UserService {

	@Autowired
    private Userrepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Token");
        }

        token = token.substring(7); // Remove "Bearer "
        String username = jwtTokenProvider.getUsernameFromToken(token);
        if (username == null) {
            throw new RuntimeException("Invalid Token - No Username Found");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}