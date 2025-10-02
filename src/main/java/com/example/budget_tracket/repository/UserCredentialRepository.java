package com.example.budget_tracket.repository;

import com.example.budget_tracket.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
	
	UserCredential findByUsername(String username);
}