package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatCreateRequest;
import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.Message;
import com.morzevichka.backend_api.mapper.ChatMapper;
import com.morzevichka.backend_api.mapper.MessageMapper;
import com.morzevichka.backend_api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final AiClientService aiClientService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ChatMapper chatMapper;

    @Transactional
    public ChatCreateResponse createChat(ChatCreateRequest request) {
        log.info("Send message to AI: {}", request.message());
        AiCreateResponse aiResponse = aiClientService.createChat(request.message());
        log.info("Response from AI is {} and title {}", aiResponse.message(), aiResponse.title());

        Chat chat = Chat.builder()
                .title(aiResponse.title())
                .user(userService.getCurrentUser())
                .build();
        chatRepository.save(chat);

        messageService.createUserMessage(chat.getId(), request.message());
        Message aiMessage = messageService.createAiMessage(chat.getId(), aiResponse.message());

        return chatMapper.toChatCreateDto(chat, messageMapper.toDto(aiMessage));
    }

    public List<ChatInfoResponse> getUserChats() {
        List<Chat> chats = chatRepository.findAllByUserId(userService.getCurrentUser().getId());
        return chatMapper.toChatInfoDto(chats);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with id:" + id));
    }
}