package com.morzevichka.backend_api.infrastructure.exception.message;

public class UserChatMismatchException extends RuntimeException {
    public UserChatMismatchException(String message) {
        super(message);
    }
}
