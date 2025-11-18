package com.morzevichka.backend_api.domain.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.value.MessageType;
import com.morzevichka.backend_api.infrastructure.exception.message.MessageLengthException;
import com.morzevichka.backend_api.infrastructure.exception.message.UserChatMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final int MAX_MESSAGE_LENGTH = 500;

    public void validateMessage(User user, Chat chat, String content, MessageType type) {
        if (!isUserInChat(user, chat)) {
            throw new UserChatMismatchException(user.getEmail());
        }

        if (content.length() > MAX_MESSAGE_LENGTH) {
            throw new MessageLengthException(String.valueOf(content.length()));
        }
    }

    public boolean isUserInChat(User user, Chat chat) {
        return user.getId().equals(chat.getUser().getId());
    }
}
