package com.example.budget_tracket.model

import java.time.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="BudgetEntries")
public class BudgetEntries {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private float amount;
	private String category;
	private LocalDate date;
	private String description;
	
	@ManyToOne
    @JoinColumn(name = "user_settings_id", nullable = false)
	@JsonIgnore
	private UserCredential userCredential;
	
	public BudgetEntries() {
		
	}
	
	public BudgetEntries(float amount, String category, LocalDate date, String description) {
		this.amount = amount;
		this.category = category;
		this.date = date;
		this.description = description;
	}
	
	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUserCredential(UserCredential userCredential) {
		this.userCredential = userCredential;
	}
	
	public UserCredential getUserCredential() {
		return this.userCredential;
	}
	
}