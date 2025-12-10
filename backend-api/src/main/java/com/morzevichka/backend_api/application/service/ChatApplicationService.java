package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.ChatRepository;
import com.morzevichka.backend_api.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatApplicationService {

    private final UserApplicationService userApplicationService;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    public List<Chat> getUserChats() {
        User user = userApplicationService.getCurrentUser();

        return chatRepository.findAllByUserId(user.getId());
    }

    public Chat getChatForCurrentUser(Long chatId) {
        Chat chat = chatRepository.findById(chatId);
        User user = userApplicationService.getCurrentUser();

        chatService.isUserInChat(user.getId(), chat.getUser().getId());

        return chat;
    }

    public Chat createChat(String title, User user) {
        Chat chat = Chat.builder()
                .title(title)
                .user(user)
                .build();

        return chatRepository.save(chat);
    }

    public Chat updateChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public void deleteChat(Chat chat) {
        chatRepository.delete(chat);
    }
}
