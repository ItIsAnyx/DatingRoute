package com.morzevichka.backend_api.infrastructure.exception;

public class ContextNotFoundException extends NotFoundException {
    public ContextNotFoundException(Long id) {
        super("Context with chat_id: " + id + " not found");
    }
}
