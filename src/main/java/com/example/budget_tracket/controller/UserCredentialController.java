package com.example.budget_tracket.controller;

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

import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.model.UserSettings;
import com.example.budget_tracket.repository.UserCredentialRepository;
import com.example.budget_tracket.repository.UserSettingsRepository;
import com.example.budget_tracket.util.Hashing;

@RestController
@RequestMapping("/UserCredential")
public class UserCredentialController {
	
	@Autowired
	public UserCredentialRepository userCredentialRepository;
	
	@Autowired
	public UserSettingsRepository userSettingsRepository;
	
	@Autowired
	public UserSettingsController userSettingsController;
	
	@Autowired
	public Hashing hashing;
	
	@GetMapping
	public List<UserCredential> getAllUserCredential() {
		return userCredentialRepository.findAll();
	}
	
    @GetMapping("/all")
    public String printAllCredentials() {
    	List<UserCredential> userCredentials = this.getAllUserCredential();
    	if(userCredentials.size() == 0) {
    		return "There is nothing in here stupid.";
    	}
    	String result = "";
    	for(int i = 0; i < userCredentials.size(); ++i) {
    		String currentCredential = "Username: " + userCredentials.get(i).getUsername() + "| Password Hash: " + userCredentials.get(i).getPasswordSalt() + "| Password: " + userCredentials.get(i).getPassword() + "\n";
    		result += currentCredential;
    	}
    	return result;
    }
    
    @GetMapping("/remove_all")
    public String removeAllCredentials() {
    	userCredentialRepository.deleteAll();
    	userCredentialRepository.flush();
    	return "All data from table user_credentials have been removed.";
    }
    
 
    @RequestMapping(value = "/update_username", method = RequestMethod.POST)
    public String updateUsername(@RequestParam String oldUsername, @RequestParam String newUsername, @RequestParam String password) {
    	UserCredential credential = userCredentialRepository.findByUsername(oldUsername);
    	UserSettings userSettings = userSettingsRepository.findByUsername(oldUsername);
    	if(credential == null) {
    		throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
    	}
    	if(!hashing.verifyPassword(password, credential.getPasswordSalt(), credential.getPassword())) {
			throw new InvalidCredentialsException(InvalidCredentialsError.INCORRECT_PASSWORD);
		}
    	credential.setUsername(newUsername);
    	userCredentialRepository.save(credential);
    	if(userSettings != null) {
    		userSettings.setUsername(newUsername);
    		userSettingsRepository.save(userSettings);
    	}
    	return "Account username updated.";
    }
    
    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    public String updatePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
    	UserCredential credential = userCredentialRepository.findByUsername(username);
    	if(credential == null) {
    		throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
    	}
    	if(!hashing.verifyPassword(oldPassword, credential.getPasswordSalt(), credential.getPassword())) {
			throw new InvalidCredentialsException(InvalidCredentialsError.INCORRECT_PASSWORD);
		}
    	ArrayList<byte[]> passwordInfo = hashing.generatePasswordHash(newPassword);
    	credential.setPasswordSalt(passwordInfo.get(0));
    	credential.setPassword(passwordInfo.get(1));
    	userCredentialRepository.save(credential);
    	return "Account password updated.";
    }
   
}