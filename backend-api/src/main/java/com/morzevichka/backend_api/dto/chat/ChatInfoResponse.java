package com.morzevichka.backend_api.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatInfoResponse(
        @NotBlank
        Long id,
        @NotBlank
        String title
) {}
