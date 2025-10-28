package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.entity.Chat;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.exception.ChatNotFoundException;
import com.morzevichka.backend_api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public List<Chat> getUserChats() {
        return chatRepository.findAllByUserId(userService.getCurrentUser().getId());
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException(id));
    }

    public Chat createAndSaveChat(String title, User user) {
        return saveChat(Chat.builder()
                .title(title)
                .user(user)
                .build()
        );
    }

    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat getReferenceByChatId(Long chatId) {
        return chatRepository.getReferenceById(chatId);
    }
}