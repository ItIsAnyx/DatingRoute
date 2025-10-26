package com.morzevichka.backend_api.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long id) {
        super("Chat with id: " + id + " not found");
    }
}
