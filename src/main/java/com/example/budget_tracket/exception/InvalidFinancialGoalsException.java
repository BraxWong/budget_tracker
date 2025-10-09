package com.example.budget_tracket.exception;

public class InvalidFinancialGoalsException extends RuntimeException {
	public enum InvalidFinancialGoalsError {
		FINANCIAL_GOAL_NOT_FOUND;
	}
	
	private final String message;
	
	public InvalidFinancialGoalsException(InvalidFinancialGoalsError error) {
		this.message = generateErrorMessage(error);
	}
	
	public String generateErrorMessage(InvalidFinancialGoalsError error) {
		if(error == InvalidFinancialGoalsError.FINANCIAL_GOAL_NOT_FOUND) {
			return "The financial goal is not found.";
		} else {
			return "Error is not found.";
		}
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}