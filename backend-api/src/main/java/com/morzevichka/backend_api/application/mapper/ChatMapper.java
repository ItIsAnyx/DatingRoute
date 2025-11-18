package com.morzevichka.backend_api.application.mapper;

import com.morzevichka.backend_api.api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.api.dto.chat.ChatResponse;
import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.domain.model.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatResponse toResponse(Chat chat) {
        return new ChatResponse(chat.getId(), chat.getTitle());
    }

    public ChatCreateResponse toCreateResponse(Chat chat, MessageResponse messageResponse) {
        return new ChatCreateResponse(chat.getId(), chat.getTitle(), messageResponse);
    }
}
