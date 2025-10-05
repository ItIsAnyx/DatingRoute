package com.morzevichka.backend_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ChatCreateRequest(
        @NotBlank
        @JsonProperty("first_message")
        String firstMessage
) {}
