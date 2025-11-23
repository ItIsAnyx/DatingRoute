package com.morzevichka.backend_api.infrastructure.exception.message;

public class MessageLengthException extends RuntimeException {
    public MessageLengthException(int length, int maxLength) {
        super("Length: " + length + " is overflow the limit: " + maxLength);
    }
}
