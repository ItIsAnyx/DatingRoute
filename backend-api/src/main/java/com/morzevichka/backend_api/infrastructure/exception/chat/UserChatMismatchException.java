package com.morzevichka.backend_api.infrastructure.exception.chat;

import com.morzevichka.backend_api.infrastructure.exception.MismatchException;

public class UserChatMismatchException extends MismatchException {
    public UserChatMismatchException(Long userId, Long chatId) {
        super("Chat with id: " + chatId + " doesn't belong to user with id:" + userId);
    }
}
