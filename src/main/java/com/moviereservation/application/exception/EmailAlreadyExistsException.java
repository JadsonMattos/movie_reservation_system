package com.moviereservation.application.exception;

public class EmailAlreadyExistsException extends ApplicationException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
