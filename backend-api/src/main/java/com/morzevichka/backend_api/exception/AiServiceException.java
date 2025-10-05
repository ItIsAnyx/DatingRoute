package com.morzevichka.backend_api.exception;

public class AiServiceException extends RuntimeException {
    public AiServiceException(String message) {
        super(message);
    }
}
