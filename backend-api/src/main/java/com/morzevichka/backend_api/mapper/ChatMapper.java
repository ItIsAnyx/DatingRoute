package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.entity.Chat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public ChatInfoResponse toDto(Chat chat) {
        return new ChatInfoResponse(chat.getId(), chat.getTitle());
    }

    public List<ChatInfoResponse> toDto(List<Chat> chats) {
        return chats
                .stream()
                .map(this::toDto)
                .toList();
    }
}
