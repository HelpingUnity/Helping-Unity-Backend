package com.helpinunitygrp.helpingunity.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpinunitygrp.helpingunity.model.User;
import com.helpinunitygrp.helpingunity.repository.Userrepository;
import com.helpinunitygrp.helpingunity.security.JwtTokenProvider;
import com.helpinunitygrp.helpingunity.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

	 @Autowired
	 private UserService userService;

	    @GetMapping("/me")
	    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
	        try {
	        	System.out.println("Received Token: " + token);
	        	// Remove "Bearer " prefix if present
	            if (token != null && token.startsWith("Bearer ")) {
//	                token = token.substring(7);
	            }
	            User user = userService.getUserFromToken(token);
	            return ResponseEntity.ok(user);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	        }
	    }
	}