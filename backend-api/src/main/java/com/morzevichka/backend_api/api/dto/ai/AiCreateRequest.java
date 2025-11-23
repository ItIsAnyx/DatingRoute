package com.morzevichka.backend_api.api.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record AiCreateRequest(
        @NotBlank
        String message
) {}
