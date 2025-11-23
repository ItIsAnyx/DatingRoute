package com.morzevichka.backend_api.domain.service;

import com.morzevichka.backend_api.infrastructure.exception.chat.BlankTitleException;
import com.morzevichka.backend_api.infrastructure.exception.chat.UserChatMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    public void isUserInChat(Long userId, Long chatUserId) {
        if (userId.compareTo(chatUserId) != 0) {
            throw new UserChatMismatchException(userId, chatUserId);
        }
    }

    public void isTitleEmpty(String title) {
        if (title.isBlank()) {
            throw new BlankTitleException();
        }
    }
}
