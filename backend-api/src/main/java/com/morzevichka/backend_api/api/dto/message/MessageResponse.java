package com.morzevichka.backend_api.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morzevichka.backend_api.domain.value.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class MessageResponse {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("chat_id")
    private Long chatId;
    private String content;
    @JsonProperty("message_type")
    private MessageType messageType;
    @JsonProperty("send_date")
    private LocalDateTime sendDate;
}
