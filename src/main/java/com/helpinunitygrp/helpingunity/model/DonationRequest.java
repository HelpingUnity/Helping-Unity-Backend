package com.helpinunitygrp.helpingunity.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "donation_requests")
public class DonationRequest {
    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the recipient
	 */
	public User getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the donationType
	 */
	public DonationType getDonationType() {
		return donationType;
	}
	/**
	 * @param donationType the donationType to set
	 */
	public void setDonationType(DonationType donationType) {
		this.donationType = donationType;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * @return the amountNeeded
	 */
	public Double getAmountNeeded() {
		return amountNeeded;
	}
	/**
	 * @param amountNeeded the amountNeeded to set
	 */
	public void setAmountNeeded(Double amountNeeded) {
		this.amountNeeded = amountNeeded;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    private String title;
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
	private String description;
    private String status;
    private LocalDateTime createdAt;
    private Double amountNeeded;
    
    
    /**
	 * @return the trusteeComment
	 */
	public String getTrusteeComment() {
		return trusteeComment;
	}
	/**
	 * @param trusteeComment the trusteeComment to set
	 */
	public void setTrusteeComment(String trusteeComment) {
		this.trusteeComment = trusteeComment;
	}
	/**
	 * @return the trusteeId
	 */
	public Long getTrusteeId() {
		return trusteeId;
	}
	/**
	 * @param trusteeId the trusteeId to set
	 */
	public void setTrusteeId(Long trusteeId) {
		this.trusteeId = trusteeId;
	}


	private String trusteeComment;
    private Long trusteeId;
    /**
	 * @return the commentDate
	 */
	public LocalDateTime getCommentDate() {
		return commentDate;
	}
	/**
	 * @param commentDate the commentDate to set
	 */
	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}
	private LocalDateTime commentDate;
	
	
	 /**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	private String imageUrl;
	
	
	
    /**
	 * @return the amountReceived
	 */
	public Double getAmountReceived() {
		return amountReceived;
	}
	/**
	 * @param amountReceived the amountReceived to set
	 */
	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}
	private Double amountReceived;
    
}