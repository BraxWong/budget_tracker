package com.example.budget_tracket.controller;

import com.example.budget_tracket.exception.InvalidBudgetEntriesException;
import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidBudgetEntriesException.InvalidBudgetEntriesError;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.model.UserSettings;
import com.example.budget_tracket.repository.BudgetEntriesRepository;
import com.example.budget_tracket.repository.UserCredentialRepository;
import com.example.budget_tracket.repository.UserSettingsRepository;
import com.example.budget_tracket.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BudgetTracker")
public class BudgetTrackerController {
	
	@Autowired
	BudgetEntriesRepository budgetEntriesRepository;
	
	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@Autowired
	UserSettingsRepository userSettingsRepository;
	
	@Autowired
	Util util;

	
	@GetMapping("/get_total_savings")
	public float getTotalSavings(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		float monthlyIncome = userSettings.getMonthlyIncome();
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		LocalDate currentMonth = LocalDate.now();
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonth() == currentMonth.getMonth()) {
				monthlyIncome -= budgetEntry.getAmount();
			}
		}
		return monthlyIncome;
	}
	
	@GetMapping("/get_total_savings_by_month")
	public float getTotalSavingsByMonth(@RequestParam String username, @RequestParam int month) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		float monthlyIncome = userSettings.getMonthlyIncome();
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonthValue() == month) {
				monthlyIncome -= budgetEntry.getAmount();
			}
		}
		return monthlyIncome;
	}
	
	@GetMapping("get_total_expenses")
	public float getTotalExpenses(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		LocalDate currentMonth = LocalDate.now();
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		float totalExpenses = 0.f;
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonth() == currentMonth.getMonth()) {
				totalExpenses += budgetEntry.getAmount();
			}
		}
		return totalExpenses;
	}
	
	@GetMapping("get_total_expenses_by_month")
	public float getTotalExpensesByMonth(@RequestParam String username, @requestParam int month) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		float totalExpenses = 0.f;
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonthValue() == month) {
				totalExpenses += budgetEntry.getAmount();
			}
		}
		return totalExpenses;
	}
	
	@GetMapping("get_total_expenses_by_category")
	public float getTotalExpensesByMonth(@RequestParam String username, @requestParam String category) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		LocalDate currentMonth = LocalDate.now();
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		float totalExpenses = 0.f;
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonth() == currentMonth.getMonth() && budgetEntry.getCategory().equals(category)) {
				totalExpenses += budgetEntry.getAmount();
			}
		}
		return totalExpenses;
	}
	
}