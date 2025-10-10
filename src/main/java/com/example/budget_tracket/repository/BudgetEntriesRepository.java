package com.example.budget_tracket.repository

import com.example.budget_tracket.model.BudgetEntries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetEntriesRepository extends JpaRepository<BudgetEntries, Long> {
	
	List<BudgetEntries> findByUsername(String username);
	
}