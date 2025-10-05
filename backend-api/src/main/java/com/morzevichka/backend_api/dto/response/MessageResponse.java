package com.morzevichka.backend_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morzevichka.backend_api.entity.MessageType;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("chat_id")
        Long chatId,
        String content,
        @JsonProperty("message_type")
        MessageType messageType,
        @JsonProperty("send_date")
        LocalDateTime sendDate
) {}
