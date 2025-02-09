package com.models;

import java.time.LocalDate;

public class BannedKeyword {
	private Long id;
	private String keyword;
	private boolean isActive;
	private LocalDate createdAt;
	private LocalDate updatedAt;

	// Constructor
	public BannedKeyword() {
	}

	public BannedKeyword(Long id, String keyword, boolean isActive, LocalDate createdAt, LocalDate updatedAt) {
		this.id = id;
		this.keyword = keyword;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
}
