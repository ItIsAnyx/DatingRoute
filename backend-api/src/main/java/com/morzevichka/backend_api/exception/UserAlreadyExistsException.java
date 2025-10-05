package com.morzevichka.backend_api.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super("User with email " + message + " is already exists");
    }
}
