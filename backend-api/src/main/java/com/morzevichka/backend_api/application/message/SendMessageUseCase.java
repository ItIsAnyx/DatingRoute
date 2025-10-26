package com.morzevichka.backend_api.application.message;

import com.morzevichka.backend_api.dto.ai.AiResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Context;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final AiClientService aiClientService;
    private final MessageMapper messageMapper;
    private final ContextService contextService;
    private final ChatService chatService;
    private final MessageService messageService;

    @Transactional
    public MessageResponse execute(MessageRequest request) {
        Context context = contextService.findContextByChatId(request.chatId());

        AiResponse aiResponse = aiClientService.sendMessage(request.message(), context);

        Message userMessage = messageService.createUserMessage(request.chatId(), request.message());
        Message aiMessage = messageService.createAiMessage(request.chatId(), aiResponse.message());

        Chat chat = chatService.getReferenceByChatId(context.getChatId());
        contextService.saveContext(chat, userMessage.getContent(), aiMessage.getContent());

        return messageMapper.toDto(aiMessage);
    }
}
