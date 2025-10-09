package com.example.budget_tracket.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    public enum InvalidCredentialsError {
        PASSWORD_LENGTH,
        PASSWORD_DUPLICATION,
        INCORRECT_PASSWORD,
        USERNAME_DUPLICATION,
        USERNAME_NOT_FOUND;
    }

    private final String message;

    public InvalidCredentialsException(InvalidCredentialsError error) {
        this.message = generateErrorMessage(error);
    }

    private String generateErrorMessage(InvalidCredentialsError error) {
        if (error == InvalidCredentialsError.PASSWORD_LENGTH) {
            return "The password has to be 14 characters or longer";
        } else if (error == InvalidCredentialsError.PASSWORD_DUPLICATION){
            return "The password has already been used";
        } else if (error == InvalidCredentialsError.INCORRECT_PASSWORD) {
        	return "The password is incorrect.";
        } else if (error == InvalidCredentialsError.PASSWORD_DUPLICATION){
        	return "The username has already been used";
        } else {
        	return "The username cannot be found";
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}