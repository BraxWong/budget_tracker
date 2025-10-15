package com.example.budget_tracket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.json.*;

import com.example.budget_tracket.util.Util;
import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.exception.InvalidFileException;
import com.example.budget_tracket.exception.InvalidFileException.InvalidFileError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.repository.UserCredentialRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController 
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	Util util;
	
	@Autowired
	UserCredentialRepository userCredentialRepository;
	
	@PostMapping("/import_json")
	public void importEntriesWithJSON(@RequestParam String username, @RequestParam("file") MultipartFile file) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		if(file.isEmpty()) {
			throw new InvalidFileException(InvalidFileError.FILE_EMPTY);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		try {
			String content = new String (file.getBytes());
			JSONObject obj = new JSONObject(content);
			JSONArray entriesArray = obj.getJSONArray("entries");
			for(int i = 0; i < entriesArray.length(); ++i) {
				float amount = entriesArray.getJSONObject(i).getFloat("amount");
				String category = entriesArray.getJSONObject(i).getString("category");
				LocalDate date = util.stringToLocalDate(entriesArray.getJSONObject(i).getString("date"));
				String description = entriesArray.getJSONObject(i).getString("description");
				BudgetEntries budgetEntry = new BudgetEntries(amount, category, date, description);
				budgetEntry.setUserCredential(userCredential);
				budgetEntries.add(budgetEntry);
			}
			userCredential.setBudgetEntries(budgetEntries);
			userCredentialRepository.save(userCredential);
		} catch (IOException e) {
			throw new InvalidFileException(InvalidFileError.FILE_INVALID);
		}
	}
	
	@PostMapping("/import_csv")
	public void importEntriesWithCSV(@RequestParam String username, @RequestParam("file") MultipartFile file) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		if(file.isEmpty()) {
			throw new InvalidFileException(InvalidFileError.FILE_EMPTY);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		try {
			String content = new String (file.getBytes());
			String[] lines = content.split("\n");
			String[][] values = new String[lines.length][];
			for(int i = 0; i < lines.length; ++i) {
				values[i] = lines[i].split(",");
			}
			for(int i = 0; i < values.length; ++i) {
				float amount = Float.parseFloat(values[i][0]);
				String category = values[i][1];
				LocalDate date = util.stringToLocalDate(values[i][2]);
				String description = values[i][3];
				BudgetEntries budgetEntry = new BudgetEntries(amount, category, date, description);
				budgetEntry.setUserCredential(userCredential);
				budgetEntries.add(budgetEntry);
			}
			userCredential.setBudgetEntries(budgetEntries);
			userCredentialRepository.save(userCredential);
		} catch (IOException e) {
			throw new InvalidFileException(InvalidFileError.FILE_INVALID);
		}
	}
	
	@RequestMapping("/export_json")
	public MultipartFile exportEntriesToJSON(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		if(file.isEmpty()) {
			throw new InvalidFileException(InvalidFileError.FILE_EMPTY);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
	}
}