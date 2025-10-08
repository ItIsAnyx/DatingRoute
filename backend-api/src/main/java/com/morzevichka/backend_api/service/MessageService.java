package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.entity.MessageType;
import com.morzevichka.backend_api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public List<Message> getMessagesByChatId(Long id) {
        return messageRepository.getAllByChatId(id);
    }

    public Message createMessage(Chat chat, String content, boolean isUser) {
        Message message = Message.builder()
                .content(content)
                .messageType(isUser ? MessageType.USER_MESSAGE : MessageType.AI_MESSAGE)
                .user(userService.getCurrentUser())
                .chat(chat)
                .build();

        return messageRepository.save(message);
    }
}
