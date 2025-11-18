package com.morzevichka.backend_api.api.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank
        @JsonProperty("refresh_token")
        String refreshToken
) {}
