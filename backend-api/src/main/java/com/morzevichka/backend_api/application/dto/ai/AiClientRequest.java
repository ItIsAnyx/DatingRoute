package com.morzevichka.backend_api.application.dto.ai;

import com.morzevichka.backend_api.domain.value.InnerContext;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record AiClientRequest(
        @NotBlank
        String message,
        List<InnerContext> context
) {}
