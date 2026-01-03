package com.morzevichka.backend_api.application.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record AiCreateClientRequest(
        @NotBlank
        String message
) {}
