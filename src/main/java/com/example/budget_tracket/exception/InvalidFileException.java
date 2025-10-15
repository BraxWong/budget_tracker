package com.example.budget_tracket.exception;

public class InvalidFileException extends RuntimeException{
	
	public enum InvalidFileError {
		FILE_EMPTY,
		FILE_INVALID
	}
	
	private final String message;
	
	public InvalidFileException(InvalidFileError invalidFileError) {
		this.message = generateErrorMessage(invalidFileError);
	}
	
	public String generateErrorMessage(InvalidFileError invalidFileError) {
		if(invalidFileError == InvalidFileError.FILE_INVALID) {
			return "The file provided is empty.";
		}
		return "The file provided is invalid.";
	}
	
    @Override
    public String getMessage() {
        return message;
    }
}