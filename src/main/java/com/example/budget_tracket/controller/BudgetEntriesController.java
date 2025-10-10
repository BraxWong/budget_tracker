package com.example.budget_tracket.controller

import com.example.budget_tracket.exception.InvalidBudgetEntriesException;
import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidBudgetEntriesException.InvalidBudgetEntriesError;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.repository.BudgetEntriesRepository;
import com.example.budget_tracket.repository.UserCredentialRepository;

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
@RequestMapping("/BudgetEntries")
public class BudgetEntriesController {
	
	@Autowired
	BudgetEntriesRepository budgetEntriesRepository;
	
	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@GetMapping("/all")
	public List<BudgetEntries> displayAllBudgetEntries() {
		List<BudgetEntries> budgetEntries = budgetEntriesRepository.findAll();
		if(budgetEntries.isEmpty()) {
			return null;
		}
		return budgetEntries;
	}
	
	@GetMapping("/get_entries_by_username")
	public List<BudgetEntries> getEntriesByUsername(String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		return userCredential.getBudgetEntries();
	}
	
	@RequestMapping(value = "/get_current_month_entries_by_username")
	public List<BudgetEntries> getCurrentMonthEntriesByUsername(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		List<BudgetEntries> currentMonthEntries = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonth() == currentDate.getMonth()) {
				currentMonthEntries.add(budgetEntry);
			}
		}
		return currentMonthEntries;
	}
	
	@RequestMapping(value = "/add_entries", method = RequestMethod.POST)
	public void addEntries(@RequestParam String username, @RequestParam float amount, @RequestParam String category, @RequestParam LocalDate date, @RequestParam String description) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		BudgetEntries budgetEntry = new BudgetEntries(amount, category, date, description);
		budgetEntry.setUserCredential(userCredential);
		budgetEntries.add(budgetEntry);
		userCredential.setBudgetEntries(budgetEntries);
		userCredentialRepository.save(userCredential);
	}
	
	@RequestMapping(value = "/edit_entries", method = RequestMethod.POST)
	public void editEntries(@RequestParam String username, @RequestParam String oldDescription, @RequestParam float amount, @RequestParam String category, @RequestParam LocalDate date, @RequestParam String description) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		for(BudgetEntries budgetEntry: budgetEntries) {
			if(budgetEntry.getDescription().equals(oldDescription)) {
				budgetEntry.setAmount(amount);
				budgetEntry.setDate(date);
				budgetEntry.setCategory(category);
				budgetEntry.setDescription(description);
				userCredential.setBudgetEntries(budgetEntries);
				userCredentialRepository.save(userCredential);
				return;
			}
		}
		throw new InvalidBudgetEntriesException(InvalidBudgetEntriesError.ENTRIES_NOT_FOUND);
	}
	
	@RequestMapping(value = "/remove_entries", method = RequestMethod.POST)
	public void removeEntries(@RequestParam String username, @RequestParam String description) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		for(int i = 0; i < budgetEntries.size(); ++i) {
			if(budgetEntries.get(i).getDescription().equals(description)) {
				budgetEntries.remove(i);
				userCredential.setBudgetEntries(budgetEntries);
				userCredentialRepository.save(userCredential);
				return;
			}
		}
		throw new InvalidBudgetEntriesException(InvalidBudgetEntriesError.ENTRIES_NOT_FOUND);
	}
}