package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatApplicationService {

    private final UserApplicationService userApplicationService;
    private final ChatRepository chatRepository;

    public List<Chat> getUserChats() {
        User user = userApplicationService.getCurrentUser();

        return chatRepository.findAllByUserId(user.getId());
    }

    public Chat getChat(Long chatId) {
        return chatRepository.findById(chatId);
    }

    public Chat createChat(String title, User user) {
        Chat chat = Chat.builder()
                .title(title)
                .user(user)
                .build();

        return chatRepository.save(chat);
    }
}
