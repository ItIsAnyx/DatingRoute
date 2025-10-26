package com.morzevichka.backend_api.application.chat;

import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ChatCreateResponse execute(ChatCreateRequest request) {
        log.info("Send message to AI: {}", request.message());
        AiCreateResponse aiResponse = aiClientService.createChat(request.message());
        log.info("Response from AI is {} and title {}", aiResponse.message(), aiResponse.title());

        Chat chat = chatService.saveChat(aiResponse.title(), userService.getCurrentUser());

        Message userMessage = messageService.createUserMessage(chat.getId(), request.message());
        Message aiMessage = messageService.createAiMessage(chat.getId(), aiResponse.message());

        contextService.saveContext(chat, userMessage.getContent(), aiMessage.getContent());

        return chatMapper.toChatCreateDto(chat, messageMapper.toDto(aiMessage));
    }
}
