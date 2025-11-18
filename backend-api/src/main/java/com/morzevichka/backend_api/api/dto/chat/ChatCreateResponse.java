package com.morzevichka.backend_api.api.dto.chat;

import com.morzevichka.backend_api.api.dto.message.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ChatCreateResponse {
    private Long id;
    private String title;
    private MessageResponse message;
}
