package com.morzevichka.backend_api.application.mapper;

import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import com.morzevichka.backend_api.domain.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .userId(message.getUser().getId())
                .chatId(message.getChat().getId())
                .content(message.getContent())
                .messageType(message.getType())
                .sendDate(message.getSendDate())
                .build();
    }
}
