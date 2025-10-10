package com.example.budget_tracket.exception;

public class InvalidBudgetEntriesException extends RuntimeException{
	
	public enum InvalidBudgetEntriesError {
		ENTRIES_NOT_FOUND
	}
	
	private final String message;
	
	public InvalidBudgetEntriesException(InvalidBudgetEntriesError invalidBudgetEntriesError) {
		this.message = generateErrorMessage(invalidBudgetEntriesError);
	}
	
	public String generateErrorMessage(InvalidBudgetEntriesError invalidBudgetEntriesError) {
		if(invalidBudgetEntriesError == InvalidBudgetEntriesError.ENTRIES_NOT_FOUND) {
			return "This budget entry cannot be found.";
		}
		return "Unknown Error";
	}
	
    @Override
    public String getMessage() {
        return message;
    }
}