package com.example.budget_tracket.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name= "user_credentials")
public class UserCredential {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private byte[] password;
	private byte[] passwordSalt;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<BudgetEntries> budgetEntries;

	public UserCredential() {
		
	}
	
	public UserCredential(String username, byte[] password, byte[] passwordSalt, List<BudgetEntries> budgetEntries) {
		this.username = username;
		this.password = password;
		this.passwordSalt = passwordSalt;
		this.budgetEntries = budgetEntries;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public byte[] getPasswordSalt() {
		return this.passwordSalt;
	}
	
	public byte[] getPassword() {
		return this.password;
	}
	
	public List<BudgetEntries> budgetEntries() {
		return this.budgetEntries;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	
	public void setPassword(byte[] password) {
		this.password = password;
	}
	
	public void setBudgetEntries(List<BudgetEntries> budgetEntries) {
		this.budgetEntries = budgetEntries;
	}
	
	public List<BudgetEntries> getBudgetEntries(){
		return this.budgetEntries;
	}
	

}
