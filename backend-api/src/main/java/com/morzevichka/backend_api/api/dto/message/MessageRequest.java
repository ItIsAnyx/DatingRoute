package com.morzevichka.backend_api.api.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        @NotBlank(message = "Сообщение пустое")
        String message
) {}
