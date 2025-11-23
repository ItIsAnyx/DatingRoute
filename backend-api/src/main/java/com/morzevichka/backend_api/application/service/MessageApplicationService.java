package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Message;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.ChatRepository;
import com.morzevichka.backend_api.domain.repository.MessageRepository;
import com.morzevichka.backend_api.domain.service.MessageService;
import com.morzevichka.backend_api.domain.value.MessageType;
import com.morzevichka.backend_api.infrastructure.exception.chat.ChatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MessageApplicationService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;

    public Page<Message> findAllByChatId(Long chatId, Pageable pageable) {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        return messageRepository.findAllByChatId(chatId, pageable);
    }

    public Message createUserMessage(Chat chat, User user, String content) {
        return createMessage(chat, user, content, MessageType.USER_MESSAGE);
    }

    public Message createAiMessage(Chat chat, User user, String content) {
        return createMessage(chat, user, content, MessageType.AI_MESSAGE);
    }

    @Async("customExecutor")
    public CompletableFuture<Message> createUserMessageAsync(Chat chat, User user, String content) {
        return CompletableFuture.completedFuture(
                createMessage(chat, user, content, MessageType.USER_MESSAGE)
        );
    }

    @Async("customExecutor")
    public CompletableFuture<Message> createAiMessageAsync(Chat chat, User user, String content) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return CompletableFuture.completedFuture(
                createMessage(chat, user, content, MessageType.AI_MESSAGE)
        );
    }

    private Message createMessage(Chat chat, User user, String content, MessageType type) {
        Message message = Message.builder()
                .content(content)
                .type(type)
                .user(user)
                .chat(chat)
                .build();

        messageService.validateMessage(user, chat, message);

        return messageRepository.save(message);
    }
}
