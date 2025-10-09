package com.example.budget_tracket.repository;

import com.example.budget_tracket.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialGoalsRepository extends JpaRepository<FinancialGoal, Long> {
	
}