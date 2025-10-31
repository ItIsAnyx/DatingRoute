package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.chat.ChatCreateResponse;
import com.morzevichka.backend_api.dto.chat.ChatInfoResponse;
import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.entity.Chat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public ChatInfoResponse toChatInfoDto(Chat chat) {
        return new ChatInfoResponse(chat.getId(), chat.getTitle());
    }

    public List<ChatInfoResponse> toChatInfoDto(List<Chat> chats) {
        return chats
                .stream()
                .map(this::toChatInfoDto)
                .toList();
    }

    public ChatCreateResponse toChatCreateDto(Chat chat, MessageResponse messageResponse) {
        return new ChatCreateResponse(chat.getId(), chat.getTitle(), messageResponse);
    }

    public Chat toEntity(ChatInfoResponse dto) {
        return Chat.builder()
                .id(dto.id())
                .title(dto.title())
                .build();
    }
}
