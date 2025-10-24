package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.ai.AiResponse;
import com.morzevichka.backend_api.dto.message.MessageRequest;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.dto.message.PageableMessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Context;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.entity.MessageType;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final AiClientService aiClientService;
    private final MessageMapper messageMapper;
    private final ContextService contextService;
    private final ChatService chatService;

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        Context context = contextService.findContextByChatId(request.chatId());

        AiResponse aiResponse = aiClientService.sendMessage(request.message(), context);

        Message userMessage = createUserMessage(request.chatId(), request.message());
        Message aiMessage = createAiMessage(request.chatId(), aiResponse.message());

        Chat chat = chatService.getReferenceByChatId(context.getChatId());
        contextService.saveContext(chat, userMessage.getContent(), aiMessage.getContent());

        return messageMapper.toDto(aiMessage);
    }

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

    public PageableMessageResponse getMessagesByChatId(Long id, Pageable pageable) {
        List<Message> messages = messageRepository.findAllByChatId(id, pageable).toList();
        return messageMapper.toPageableDto(pageable, messages);
    }
}