package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.entity.MessageType;
import com.morzevichka.backend_api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    public Message createUserMessage(Long chatId, String content) {
        return createMessage(chatId, content, MessageType.USER_MESSAGE);
    }

    public Message createAiMessage(Long chatId, String content) {
        return createMessage(chatId, content, MessageType.AI_MESSAGE);
    }

    private Message createMessage(Long chatId, String content, MessageType type) {
        Chat chat = chatService.getReferenceByChatId(chatId);

        Message message = Message.builder()
                .content(content)
                .messageType(type)
                .user(userService.getCurrentUser())
                .chat(chat)
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long id, Pageable pageable) {
        return messageRepository.findAllByChatId(id, pageable).toList();
    }
}