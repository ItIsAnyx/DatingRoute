package com.morzevichka.backend_api.infrastructure.exception.chat;

import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;

public class ChatNotFoundException extends NotFoundException {
    public ChatNotFoundException(Long chatId) {
        super("Chat with ID:" + chatId + " not found");
    }
}
