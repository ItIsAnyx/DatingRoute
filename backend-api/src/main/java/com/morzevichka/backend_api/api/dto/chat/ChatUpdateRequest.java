package com.morzevichka.backend_api.api.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatUpdateRequest(
        @NotBlank(message = "Поле title не может быть пустым")
        String title
) {}
