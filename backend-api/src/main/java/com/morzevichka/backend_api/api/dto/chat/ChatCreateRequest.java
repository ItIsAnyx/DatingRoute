package com.morzevichka.backend_api.api.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatCreateRequest(
        @NotBlank
        String message
) {}
