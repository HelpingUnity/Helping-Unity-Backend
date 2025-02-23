package com.helpinunitygrp.helpingunity.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.MultipartFile;

import com.helpinunitygrp.helpingunity.model.DonationRequest;
import com.helpinunitygrp.helpingunity.model.User;
import com.helpinunitygrp.helpingunity.repository.DonationRequestRepository;
import com.helpinunitygrp.helpingunity.repository.Userrepository;

@Service
public class DonationRequestService {
    @Autowired
    private DonationRequestRepository donationRequestRepository;
    
    @Autowired
    private Userrepository userRepository;

    public ResponseEntity<?> createRequest(DonationRequest request, Long userId) {
        User recipient = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        request.setRecipient(recipient);
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus("PENDING");
        

        
        DonationRequest savedRequest = donationRequestRepository.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(donationRequestRepository.findAll());
    }

    public ResponseEntity<?> getRequestById(Long id) {
        return ResponseEntity.ok(donationRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found")));
    }

    public ResponseEntity<?> updateRequest(Long id, DonationRequest request, Long userId) {
        DonationRequest existingRequest = donationRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
            
        if (!existingRequest.getRecipient().getId().equals(userId)) {
            return ResponseEntity.badRequest()
                .body("You can only update your own requests");
        }
        
        existingRequest.setDescription(request.getDescription());
        existingRequest.setDonationType(request.getDonationType());
        existingRequest.setAmountNeeded(request.getAmountNeeded());
        
        return ResponseEntity.ok(donationRequestRepository.save(existingRequest));
    }

    public ResponseEntity<?> deleteRequest(Long id, Long userId) {
        DonationRequest request = donationRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
            
        if (!request.getRecipient().getId().equals(userId)) {
            return ResponseEntity.badRequest()
                .body("You can only delete your own requests");
        }
        
        donationRequestRepository.delete(request);
        return ResponseEntity.ok().build();
    }
    
    
    // Trustee Comment
    public ResponseEntity<?> updateTrusteeComment(Long id, Long trusteeId, String comment) {
        Optional<DonationRequest> donationRequestOpt = donationRequestRepository.findById(id);
        if (!donationRequestOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Donation request not found");
        }
        DonationRequest donationRequest = donationRequestOpt.get();
        // If a comment already exists by another trustee, forbid update.
        if (donationRequest.getTrusteeId() != null && !donationRequest.getTrusteeId().equals(trusteeId)) {
            return ResponseEntity.status(403).body("You can only update your own comment");
        }
        donationRequest.setTrusteeComment(comment);
        donationRequest.setTrusteeId(trusteeId);
        donationRequest.setCommentDate(LocalDateTime.now());
        donationRequestRepository.save(donationRequest);
        return ResponseEntity.ok(donationRequest);
    }
    
    public ResponseEntity<?> deleteTrusteeComment(Long id, Long trusteeId) {
        Optional<DonationRequest> donationRequestOpt = donationRequestRepository.findById(id);
        if (!donationRequestOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Donation request not found");
        }
        DonationRequest donationRequest = donationRequestOpt.get();
        if (donationRequest.getTrusteeId() == null || !donationRequest.getTrusteeId().equals(trusteeId)) {
            return ResponseEntity.status(403).body("You can only delete your own comment");
        }
        donationRequest.setTrusteeComment(null);
        donationRequest.setTrusteeId(null);
        donationRequest.setCommentDate(null);
        donationRequestRepository.save(donationRequest);
        return ResponseEntity.ok("Comment deleted");
    }
}
