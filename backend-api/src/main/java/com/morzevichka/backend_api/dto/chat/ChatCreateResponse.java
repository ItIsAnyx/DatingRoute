package com.morzevichka.backend_api.dto.chat;

import com.morzevichka.backend_api.dto.message.MessageResponse;

public record ChatCreateResponse(Long id, String title, MessageResponse message) {
}
