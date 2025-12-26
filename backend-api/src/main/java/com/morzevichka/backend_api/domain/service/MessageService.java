package com.morzevichka.backend_api.domain.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Message;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.exception.message.MessageLengthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final int MAX_MESSAGE_LENGTH = 2000;
    private final ChatService chatService;

    public void validateMessage(User user, Chat chat, Message message) {
        chatService.isUserInChat(user.getId(), chat.getUser().getId());

        if (message.getContent().length() > MAX_MESSAGE_LENGTH) {
            throw new MessageLengthException(message.getContent().length(), MAX_MESSAGE_LENGTH);
        }
    }
}
