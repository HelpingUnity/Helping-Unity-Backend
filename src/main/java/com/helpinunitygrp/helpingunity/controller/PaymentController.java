package com.helpinunitygrp.helpingunity.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpinunitygrp.helpingunity.model.DonationRequest;
import com.helpinunitygrp.helpingunity.repository.DonationRequestRepository;
import com.helpinunitygrp.helpingunity.service.DonationRequestService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class PaymentController {
    
    @Autowired
    private DonationRequestRepository donationRequestRepository;
    
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping
    public ResponseEntity<?> createCharge(@RequestBody Map<String, Object> paymentData) {
        // Set the Stripe API key
        Stripe.apiKey = stripeSecretKey;

        try {
            // Extract payment details from the request
            int amount = (Integer) paymentData.get("amount"); // in cents
            String currency = (String) paymentData.get("currency");
            // For testing, we're using a token (like "tok_visa") as the source
            String paymentMethodId = (String) paymentData.get("paymentMethodId");
            Long donationRequestId = ((Number) paymentData.get("donationRequestId")).longValue();

            // Validate that the donation request exists and calculate the remaining amount needed
            DonationRequest request = donationRequestRepository.findById(donationRequestId)
                .orElseThrow(() -> new RuntimeException("Donation request not found"));

            double amountReceived = (request.getAmountReceived() != null ? request.getAmountReceived() : 0);
            double remainingAmount = request.getAmountNeeded() - amountReceived;

            if (amount / 100.0 > remainingAmount) {
                return ResponseEntity.badRequest()
                    .body("Payment amount exceeds remaining amount needed");
            }

            // Prepare charge parameters using the Charges API
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount);
            chargeParams.put("currency", currency);
            chargeParams.put("source", paymentMethodId); // using token as source for Charge creation
            chargeParams.put("description", "Donation for request #" + donationRequestId);

            // Create the charge with Stripe
            Charge charge = Charge.create(chargeParams);

            // If the charge is successful, update the donation request
            if (charge.getPaid() != null && charge.getPaid()) {
                double newAmountReceived = amountReceived + (amount / 100.0);
                request.setAmountReceived(newAmountReceived);
                if (newAmountReceived >= request.getAmountNeeded()) {
                    request.setStatus("FUNDED");
                    
                }
                request.setAmountNeeded(request.getAmountNeeded() - (amount / 100.0));
                donationRequestRepository.save(request);
            }

            // Build a simplified response
            Map<String, Object> response = new HashMap<>();
            response.put("status", charge.getStatus());
            response.put("id", charge.getId());
            response.put("amount", charge.getAmount());
            response.put("remainingAmount", remainingAmount - (amount / 100.0));

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Stripe error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing payment: " + e.getMessage());
        }
    }
}