package com.helpinunitygrp.helpingunity.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.helpinunitygrp.helpingunity.model.DonationRequest;
import com.helpinunitygrp.helpingunity.security.UserDetailsServiceImpl;
import com.helpinunitygrp.helpingunity.security.UserPrincipal;
import com.helpinunitygrp.helpingunity.service.DonationRequestService;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
	    origins = "http://localhost:5173",  
	    allowedHeaders = "*",
	    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
	    allowCredentials = "true"
	)
@RequestMapping("/api/donation-requests")
public class DonationRequestController {
    @Autowired
    private DonationRequestService donationRequestService;

    @PostMapping
    @PreAuthorize("hasRole('RECIPIENT')")
    public ResponseEntity<?> createRequest(
            @RequestBody DonationRequest request,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        return donationRequestService.createRequest(request, userDetails.getId());
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(donationRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(donationRequestService.getRequestById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRUSTEE', 'ADMIN')")
    public ResponseEntity<?> updateRequest(
            @PathVariable Long id,
            @RequestBody DonationRequest request,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        return donationRequestService.updateRequest(id, request, userDetails.getId());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRUSTEE', 'ADMIN')")
    public ResponseEntity<?> deleteRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userDetails) {
        return donationRequestService.deleteRequest(id, userDetails.getId());
    }
    
    @PutMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('TRUSTEE', 'ADMIN')")
    public ResponseEntity<?> updateTrusteeComment(
            @PathVariable("id") Long donationRequestId,
            @RequestParam Long trusteeId,
            @RequestBody Map<String, String> payload) {
        String comment = payload.get("comment");
        return donationRequestService.updateTrusteeComment(donationRequestId, trusteeId, comment);
    }
    
    // Endpoint to delete trustee comment (only TRUSTEE role)
    @DeleteMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('TRUSTEE', 'ADMIN')")
    public ResponseEntity<?> deleteTrusteeComment(
            @PathVariable("id") Long donationRequestId,
            @RequestParam Long trusteeId) {
        return donationRequestService.deleteTrusteeComment(donationRequestId, trusteeId);
    }
    
    
    
}
