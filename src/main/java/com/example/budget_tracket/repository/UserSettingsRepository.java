package com.example.budget_tracket.repository;

import com.example.budget_tracket.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
	UserSettings findByUsername(String username);
}