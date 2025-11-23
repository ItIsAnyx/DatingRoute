package com.morzevichka.backend_api.infrastructure.exception.chat;

public class BlankTitleException extends RuntimeException {
    public BlankTitleException() {
        super("Ai create chat with empty title, try again!");
    }
}
