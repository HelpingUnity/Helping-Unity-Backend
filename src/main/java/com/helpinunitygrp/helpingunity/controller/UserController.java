// Import necessary Spring framework classes
package com.helpinunitygrp.helpingunity.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpinunitygrp.helpingunity.model.User; // Import User model
import com.helpinunitygrp.helpingunity.service.UserService; // Import UserService to interact with user data

// Define a REST controller to handle user-related API requests
@RestController
@CrossOrigin // Enable Cross-Origin requests
@RequestMapping("/api/user") // Base URL for user-related endpoints
public class UserController {

    // Inject the UserService using Spring's Dependency Injection
    @Autowired
    private UserService userService;

    // Define a GET method to fetch the user's profile data
    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("Received Token: " + token); // Log the received token for debugging

            // Check if the token starts with "Bearer " (this part is commented out)
            if (token != null && token.startsWith("Bearer ")) {
                // Optionally remove the "Bearer " prefix (currently not in use)
                // token = token.substring(7);
            }

            // Use the UserService to get the user from the token
            User user = userService.getUserFromToken(token);

            // Return the user data as a response
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // If an error occurs (e.g., invalid token), return an unauthorized response with the error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
