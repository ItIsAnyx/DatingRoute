package com.morzevichka.backend_api.application.chat;

import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreateChatUseCase {

    private final UserService userService;
    private final AiClientService aiClientService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ChatMapper chatMapper;
    private final ContextService contextService;
    private final ChatService chatService;
    private final Executor customExecutor;

    public ChatCreateResponse execute(ChatCreateRequest request) {
        User user = userService.getCurrentUser();
        AiCreateResponse aiResponse = aiClientService.createChatRequest(request.message());
        Chat chat = chatService.createAndSaveChat(aiResponse.title(), userService.getCurrentUser());

        CompletableFuture<Message> userMessageFuture = CompletableFuture.supplyAsync(
                () -> messageService.createUserMessage(chat.getId(), user, request.message()),
                customExecutor
        );
        CompletableFuture<Message> aiMessageFuture = CompletableFuture.supplyAsync(
                () -> messageService.createAiMessage(chat.getId(), user, aiResponse.message()),
                customExecutor
        );

        return userMessageFuture.thenCombine(aiMessageFuture, (userMessage, aiMessage) -> {
            CompletableFuture.runAsync(
                    () -> contextService.saveContext(chat, userMessage.getContent(), aiMessage.getContent()),
                    customExecutor
            );
            return chatMapper.toChatCreateDto(chat, messageMapper.toDto(aiMessage));
        }).join();
    }
}
