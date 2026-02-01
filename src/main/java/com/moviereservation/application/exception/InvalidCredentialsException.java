package com.moviereservation.application.exception;

public class InvalidCredentialsException extends ApplicationException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
