package com.morzevichka.backend_api.infrastructure.exception;

public class MismatchException extends RuntimeException {
    public MismatchException(String message) {
        super(message);
    }
}
