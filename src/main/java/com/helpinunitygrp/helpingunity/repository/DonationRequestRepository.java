package com.helpinunitygrp.helpingunity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpinunitygrp.helpingunity.model.DonationRequest;

public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
    List<DonationRequest> findByRecipientId(Long recipientId);
}
