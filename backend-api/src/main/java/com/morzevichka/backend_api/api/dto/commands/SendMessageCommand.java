package com.morzevichka.backend_api.api.dto.commands;

import com.morzevichka.backend_api.domain.model.User;

import java.security.Principal;

public record SendMessageCommand(
        Long chatId,
        User user,
        String content,
        Principal principal
) {
}
