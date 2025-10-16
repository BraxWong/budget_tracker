package com.example.budget_tracket.exception;

public class InvalidFileException extends RuntimeException{
	
	public enum InvalidFileError {
		FILE_EMPTY,
		FILE_INVALID,
		JSON_FILE_EXPORT_ERROR
	}
	
	private final String message;
	
	public InvalidFileException(InvalidFileError invalidFileError) {
		this.message = generateErrorMessage(invalidFileError);
	}
	
	public String generateErrorMessage(InvalidFileError invalidFileError) {
		if(invalidFileError == InvalidFileError.FILE_INVALID) {
			return "The file provided is empty.";
		} else if(invalidFileError == InvalidFileError.FILE_INVALID) {
			return "The file provided is invalid.";
		}
		return "Error when exporting to JSON file.";
	}
	
    @Override
    public String getMessage() {
        return message;
    }
}