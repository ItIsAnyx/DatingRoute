package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.response.AiChatCreateResponse;
import com.morzevichka.backend_api.dto.response.ChatCreateResponse;
import com.morzevichka.backend_api.dto.response.MessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.exception.ChatNotFoundException;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final AiClientService aiClientService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Transactional
    public ChatCreateResponse createChat(String firstMessage) {
        AiChatCreateResponse aiResponse = aiClientService.getAiChatCreateResponse(firstMessage);

        Chat chat = Chat.builder()
                .title(aiResponse.title())
                .user(userService.getCurrentUser())
                .build();

        chatRepository.save(chat);

        Message userMessage = messageService.createMessage(chat, firstMessage, true);

        Message aiMessage = messageService.createMessage(chat, aiResponse.responseText(), false);

        MessageResponse aiMessageResponse = messageMapper.toDto(aiMessage);

        return new ChatCreateResponse(chat.getId(), chat.getTitle(), aiMessageResponse);
    }

    public List<Chat> getUserChats() {
        return chatRepository.findAllByUserId(userService.getCurrentUser().getId());
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException(id));
    }
}
