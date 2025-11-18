package com.morzevichka.backend_api.infrastructure.exception.message;

public class MessageLengthException extends RuntimeException {
    public MessageLengthException(String message) {
        super(message);
    }
}
