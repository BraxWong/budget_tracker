package com.example.budget_tracket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.exception.InvalidFinancialGoalsException;
import com.example.budget_tracket.exception.InvalidFinancialGoalsException.InvalidFinancialGoalsError;
import com.example.budget_tracket.model.FinancialGoal;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.model.UserSettings;
import com.example.budget_tracket.repository.UserCredentialRepository;
import com.example.budget_tracket.repository.UserSettingsRepository;

@RestController
@RequestMapping("/UserSettings")
public class UserSettingsController {
	
	@Autowired
	UserSettingsRepository userSettingsRepository;
	
	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@GetMapping
	public List<UserSettings> getAllUserSettings() {
		return userSettingsRepository.findAll();
	}
	
	@GetMapping("/all")
	public List<UserSettings> displayUserSettings() {
		List<UserSettings> userSettings = this.getAllUserSettings();
		if(userSettings.isEmpty()) {
			return null;
		}
		return userSettings;
	}
	
	@GetMapping("/get_user_settings")
	public UserSettings getUserSettings(@RequestBody String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		if(userSettings == null) {
			List<FinancialGoal> financialGoals = new ArrayList<>();
			userSettings = new UserSettings(username, 0, 0, 0, financialGoals, false, false);
			userSettingsRepository.save(userSettings);
		}
		return userSettings;
	}
	
	@RequestMapping("/set_user_settings")
	public String setUserSettings(@RequestBody UserSettings userSettings) {
		UserCredential userCredential = userCredentialRepository.findByUsername(userSettings.getUsername());
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		UserSettings defaultSettings = userSettingsRepository.findByUsername(userSettings.getUsername());
		defaultSettings.setUsername(userSettings.getUsername());
		defaultSettings.setMonthlySpendingPercentage(userSettings.getMonthlySpendingPercentage());
		defaultSettings.setMonthlySavingPercentage(userSettings.getMonthlySavingPercentage());
		defaultSettings.setMonthlyIncome(userSettings.getMonthlyIncome());
		List<FinancialGoal> financialGoal = defaultSettings.getFinancialGoals();
		List<FinancialGoal> newFinancialGoal = userSettings.getFinancialGoals();
		defaultSettings.setFinancialGoals(Stream.concat(financialGoal.stream(), newFinancialGoal.stream()).collect(Collectors.toList()));
		defaultSettings.setNotificationEnabled(userSettings.getNotificationEnabled());
		defaultSettings.setBackupEnabled(userSettings.getBackupEnabled());
		userSettingsRepository.save(defaultSettings);
		return "User settings have been updated.";
	}
	
	@RequestMapping(value = "/add_financial_goal", method = RequestMethod.POST)
	public List<FinancialGoal> addFinancialGoal(@RequestParam String username, @RequestParam String goal, @RequestParam boolean achieved) {
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		if(userSettings == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<FinancialGoal> financialGoals = userSettings.getFinancialGoals();
		FinancialGoal financialGoal = new FinancialGoal(goal, achieved);
		financialGoal.setUserSettings(userSettings);
		financialGoals.add(financialGoal);
		userSettings.setFinancialGoals(financialGoals);
		userSettingsRepository.save(userSettings);
		return financialGoals;
	}
	
	@RequestMapping(value = "/edit_financial_goal", method = RequestMethod.POST)
	public void editFinancialGoal(@RequestParam String username, @RequestParam String oldGoal, @RequestParam String newGoal, @RequestParam boolean achieved) {
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		if(userSettings == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<FinancialGoal> financialGoals = userSettings.getFinancialGoals();
		for(FinancialGoal financialGoal: financialGoals) {
			if(financialGoal.getGoal().equals(oldGoal)) {
				financialGoal.setGoal(newGoal);
				financialGoal.setAchieved(achieved);
				userSettings.setFinancialGoals(financialGoals);
				userSettingsRepository.save(userSettings);
				return;
			}
		}
		throw new InvalidFinancialGoalsException(InvalidFinancialGoalsError.FINANCIAL_GOAL_NOT_FOUND);
	}
	
	@RequestMapping("/remove_financial_goal")
	public void removeFinancialGoal(@RequestParam String username, @RequestParam String goal) {
		UserSettings userSettings = userSettingsRepository.findByUsername(username);
		if(userSettings == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<FinancialGoal> financialGoals = userSettings.getFinancialGoals();
		for(int i = 0; i < financialGoals.size(); ++i) {
			if(financialGoals.get(i).getGoal().equals(goal)) {
				financialGoals.remove(i);
				userSettings.setFinancialGoals(financialGoals);
				userSettingsRepository.save(userSettings);
				return;
			}
		}
		throw new InvalidFinancialGoalsException(InvalidFinancialGoalsError.FINANCIAL_GOAL_NOT_FOUND);
	}
	
}
