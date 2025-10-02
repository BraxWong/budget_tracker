package com.example.budget_tracket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
@Table(name= "user_credentials")
public class UserCredential {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private byte[] password;
	private byte[] passwordSalt;
	

	public UserCredential() {
		
	}
	
	public UserCredential(String username, byte[] password, byte[] passwordSalt) {
		this.username = username;
		this.password = password;
		this.passwordSalt = passwordSalt;
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

}
