package com.helpinunitygrp.helpingunity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.helpinunitygrp.helpingunity.model.User;
import com.helpinunitygrp.helpingunity.repository.Userrepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private Userrepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        return UserPrincipal.build(user);
    }
    
    // Add this method for JWT authentication
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        
        return UserPrincipal.build(user);
    }
}

