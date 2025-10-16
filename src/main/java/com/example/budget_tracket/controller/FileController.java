package com.example.budget_tracket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.json.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.example.budget_tracket.util.Util;
import com.example.budget_tracket.exception.InvalidCredentialsException;
import com.example.budget_tracket.exception.InvalidCredentialsException.InvalidCredentialsError;
import com.example.budget_tracket.exception.InvalidFileException;
import com.example.budget_tracket.exception.InvalidFileException.InvalidFileError;
import com.example.budget_tracket.model.BudgetEntries;
import com.example.budget_tracket.model.UserCredential;
import com.example.budget_tracket.repository.UserCredentialRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
	public ResponseEntity<byte[]> exportEntriesToJSON(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		List<BudgetEntries> newList = new ArrayList<>();
		LocalDate today = LocalDate.now();
		for(int i = 0; i < budgetEntries.size(); ++i) {
			LocalDate date = budgetEntries.get(i).getDate();
			if(date.getMonth() == today.getMonth()) {
				newList.add(budgetEntries.get(i));
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
            byte[] jsonBytes = mapper.writeValueAsBytes(newList);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=budget_entries.json");
            headers.add("Content-Type", "application/json");

            return new ResponseEntity<>(jsonBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new InvalidFileException(InvalidFileError.JSON_FILE_EXPORT_ERROR);
       }	
	}
	
	@RequestMapping("/export_csv")
	public ResponseEntity<byte[]> exportEntriesToCSV(@RequestParam String username) {
		UserCredential userCredential = userCredentialRepository.findByUsername(username);
		if(userCredential == null) {
			throw new InvalidCredentialsException(InvalidCredentialsError.USERNAME_NOT_FOUND);
		}
		List<BudgetEntries> budgetEntries = userCredential.getBudgetEntries();
		String csv = "";
		LocalDate today = LocalDate.now();
		for(BudgetEntries budgetEntry : budgetEntries) {
			if(budgetEntry.getDate().getMonth() == today.getMonth()) {
				csv += (Float.toString(budgetEntry.getAmount()) + ",");
				csv += (budgetEntry.getCategory() + ",");
				csv += (budgetEntry.getDate().toString() + ",");
				csv += (budgetEntry.getDescription() + "\n");
			}
		}
		
        byte[] jsonBytes = csv.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=budget_entries.csv");
        headers.add("Content-Type", "text/csv");

        return new ResponseEntity<>(jsonBytes, headers, HttpStatus.OK);
       
	}
	
}