package com.morzevichka.backend_api.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull(message = "chat_id не указан")
        @JsonProperty("chat_id")
        Long chatId,
        @NotBlank(message = "Сообщение пустое")
        String message
) {}
