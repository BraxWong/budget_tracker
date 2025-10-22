package com.example.budget_tracket.controller;

import com.example.budget_tracket.exception.InvalidBudgetEntriesException;
import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidBudgetEntriesException.InvalidBudgetEntriesError;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.repository.BudgetEntriesRepository;
import com.example.budget_tracket.repository.UserCredentialRepository;
import com.example.budget_tracket.util.Hashing;
import com.example.budget_tracket.util.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicAPIController {
	
	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@Autowired
	public UserSettingsController userSettingsController;
	
	@Autowired
	public Hashing hashing;
	
	 @RequestMapping("/create_ac")
	    public void createUserAccount(@RequestParam String username, @RequestParam String password) {
	        if (password.length() < 14) {
	            throw new InvalidCredentialsException(InvalidCredentialsError.PASSWORD_LENGTH);
	        }
	        if(userCredentialRepository.findByUsername(username) != null) {
	        	throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_DUPLICATION);
	        }
	        ArrayList<byte[]> passwordInfo = hashing.generatePasswordHash(password);
	        List<BudgetEntries> budgetEntries = new ArrayList<>();
	        UserCredential userCredential = new UserCredential(username, passwordInfo.get(1), passwordInfo.get(0), budgetEntries);
	        userCredentialRepository.save(userCredential);
	        userSettingsController.getUserSettings(username);
	    }
	    
	    @RequestMapping("/login")
	    public String userLogin(@RequestParam String username, @RequestParam String password) {
	    	UserCredential credential = userCredentialRepository.findByUsername(username);
	    	if(credential == null) {
	    		throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
	    	}
	    	if(hashing.verifyPassword(password, credential.getPasswordSalt(), credential.getPassword())) {
				return "Login Success";
			}
			else {
				return "Incorrect Password";
			}
	    }
	
}