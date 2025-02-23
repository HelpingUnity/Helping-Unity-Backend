package com.helpinunitygrp.helpingunity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpinunitygrp.helpingunity.model.Role;
import com.helpinunitygrp.helpingunity.model.User;

@Repository
public interface Userrepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}
