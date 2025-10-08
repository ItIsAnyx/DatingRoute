package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.message.MessageResponse;
import com.morzevichka.backend_api.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageMapper {

    public MessageResponse toDto(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getUser().getId(),
                message.getChat().getId(),
                message.getContent(),
                message.getMessageType(),
                message.getSendDate()
        );
    }

    public List<MessageResponse> toDto(List<Message> messages) {
        return messages
                .stream()
                .map(this::toDto)
                .toList();
    }
}
