package com.example.budget_tracket.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="FinancialGoal")
public class FinancialGoal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String goal;
	private boolean achieved;
	
	//TODO: Should add goal start date and end date, need to modify the table for that as well
	
	@ManyToOne
    @JoinColumn(name = "user_settings_id", nullable = false)
	@JsonIgnore
	private UserSettings userSettings;
	
	public FinancialGoal() {
		
	}
	
	public FinancialGoal(String goal, boolean achieved) {
		this.goal = goal;
		this.achieved = achieved;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public boolean isAchieved() {
		return achieved;
	}

	public void setAchieved(boolean achieved) {
		this.achieved = achieved;
	}
	
	public void setUserSettings(UserSettings userSettings) {
		this.userSettings = userSettings;
	}
	
	public UserSettings getUserSettings() {
		return this.userSettings;
	}
}