package com.morzevichka.backend_api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        @JsonProperty("chat_id")
        @NotBlank
        Long chatId,
        @NotBlank
        String message
) {}
