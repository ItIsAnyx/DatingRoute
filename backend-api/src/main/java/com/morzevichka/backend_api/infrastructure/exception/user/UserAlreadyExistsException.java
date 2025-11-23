package com.morzevichka.backend_api.infrastructure.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with email: " + email + " is already exists");
    }
}
