// Import necessary Spring framework and application classes
package com.helpinunitygrp.helpingunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpinunitygrp.helpingunity.model.User; // Import User model
import com.helpinunitygrp.helpingunity.repository.Userrepository; // Import UserRepository to interact with user data in the database
import com.helpinunitygrp.helpingunity.security.JwtTokenProvider; // Import JwtTokenProvider to handle JWT token operations

// Annotate this class as a service class, making it available for Dependency Injection
@Service
public class UserService {

    // Inject UserRepository using Spring's Dependency Injection
    @Autowired
    private Userrepository userRepository;

    // Inject JwtTokenProvider to work with JWT tokens
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Method to extract user data from a JWT token
    public User getUserFromToken(String token) {
        // Check if the token is null or does not start with "Bearer "
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Token"); // Throw an error if the token is invalid
        }

        token = token.substring(7); // Remove the "Bearer " prefix from the token
        Long userId = Long.parseLong(jwtTokenProvider.getUsernameFromToken(token)); // Extract user ID from the token
        if (userId == 0) {
            throw new RuntimeException("Invalid Token - No Username Found"); // Throw an error if no user ID is found
        }

        // Find the user by ID in the database, or throw an error if not found
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
