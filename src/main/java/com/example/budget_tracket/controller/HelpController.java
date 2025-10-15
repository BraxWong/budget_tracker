package com.example.budget_tracket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HelpController {
	@GetMapping("/")
	public String index() {
		return "Budget Tracker 1.0.0   21/09/2025. Maintained by Brax.W.\n" +
		"/login      	 Log into the system\n" +
		"/logout     	 Log out of the system\n" +
		"/create_ac      Create a new account\n" +
		"/settings   	 View monthly saving settings\n" +
		"/settings_u     Update monthly saving settings\n" +
		"/overview       Insight on current month savings\n" +     
		"/input      	 Insert new entry to this month's budget\n" +
		"/input_f    	 Insert new entries to this month's budget in a file format\n" +
		"/input_h    	 Provide help on the input file format for inserting new entries.\n" +
		"/update	     Update an entry based on user's input\n" +
		"/remove     	 Remove an entry based on user's input\n" +
		"/output         Output this month's budget to local machine\n" + 
		"/help_{command} Provide help on the command\n";
	}
}