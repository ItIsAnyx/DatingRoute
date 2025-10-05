package com.morzevichka.backend_api.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long id) {
        super("Chat not found with id: " + id);
    }
}
