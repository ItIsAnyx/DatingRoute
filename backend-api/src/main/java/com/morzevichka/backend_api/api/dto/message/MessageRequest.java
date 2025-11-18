package com.morzevichka.backend_api.api.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        @NotBlank
        @JsonProperty("chat_id")
        Long chatId,
        @NotBlank
        String message
) {}
