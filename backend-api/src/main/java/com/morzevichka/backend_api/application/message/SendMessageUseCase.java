package com.morzevichka.backend_api.application.message;

import com.morzevichka.backend_api.dto.ai.AiResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Context;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final AiClientService aiClientService;
    private final MessageMapper messageMapper;
    private final ContextService contextService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final Executor customExecutor;

    public MessageResponse execute(MessageRequest request) {
        User user = userService.getCurrentUser();

        Context context = contextService.findContextByChatId(request.chatId());

        CompletableFuture<Message> userMessageFuture = CompletableFuture.supplyAsync(
               () -> messageService.createUserMessage(request.chatId(), user, request.message()),
               customExecutor
        );
        CompletableFuture<Message> aiMessageFuture = CompletableFuture.supplyAsync(
                () -> {
                    AiResponse aiResponse = aiClientService.sendMessageRequest(request.message(), context);
                    return messageService.createAiMessage(request.chatId(), user, aiResponse.message());
                },
                customExecutor
        );

        return userMessageFuture.thenCombine(aiMessageFuture, (userMessage, aiMessage) -> {
            Chat chat = chatService.getReferenceByChatId(context.getChatId());
            contextService.saveContext(chat, userMessage.getContent(), aiMessage.getContent());

            return messageMapper.toDto(aiMessage);
        }).join();
    }
}
