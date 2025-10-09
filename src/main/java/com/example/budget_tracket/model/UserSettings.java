package com.example.budget_tracket.model;


import java.util.List;
import java.util.Map;

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
@Table(name= "UserSettings")
public class UserSettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private int monthlySpendingPercentage;
	private int monthlySavingPercentage;
	private float monthlyIncome;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<FinancialGoal> financialGoals;
	private boolean notificationEnabled;
	private boolean backupEnabled;
	
	public UserSettings() {
		
	}
	
	public UserSettings(String username, int monthlySpendingPercentage, int monthlySavingPercentage, float monthlyIncome, List<FinancialGoal> financialGoals, boolean notificationEnabled, boolean backupEnabled ) {
		this.username = username;
		this.monthlySpendingPercentage = monthlySpendingPercentage;
		this.monthlySavingPercentage = monthlySavingPercentage;
		this.monthlyIncome = monthlyIncome;
		this.financialGoals = financialGoals;
		this.notificationEnabled = notificationEnabled;
		this.backupEnabled = backupEnabled;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setMonthlySpendingPercentage(int monthlySpendingPercentage) {
		this.monthlySpendingPercentage = monthlySpendingPercentage;
	}
	
	public void setMonthlySavingPercentage(int monthlySavingPercentage) {
		this.monthlySavingPercentage = monthlySavingPercentage;
	}
	
	public void setMonthlyIncome(float monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	
	public void setFinancialGoals(List<FinancialGoal> financialGoals) {
		this.financialGoals = financialGoals;
	}
	
	public void setNotificationEnabled(boolean notificationEnabled) {
		this.notificationEnabled = notificationEnabled;
	}
	
	public void setBackupEnabled(boolean backupEnabled) {
		this.backupEnabled = backupEnabled;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getMonthlySpendingPercentage() {
		return this.monthlySpendingPercentage;
	}
	
	public int getMonthlySavingPercentage() {
		return this.monthlySavingPercentage;
	}
	
	public float getMonthlyIncome() {
		return this.monthlyIncome;
	}
	
	public List<FinancialGoal> getFinancialGoals() {
		return this.financialGoals;
	}
	
	public boolean getNotificationEnabled() {
		return this.notificationEnabled;
	}
	
	public boolean getBackupEnabled() {
		return this.backupEnabled;
	}
}