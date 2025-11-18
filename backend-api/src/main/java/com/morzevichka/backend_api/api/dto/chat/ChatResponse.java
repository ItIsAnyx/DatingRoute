package com.morzevichka.backend_api.api.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ChatResponse {
    private Long id;
    private String title;
}
