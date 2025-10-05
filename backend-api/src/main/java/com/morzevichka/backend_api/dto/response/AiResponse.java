package com.morzevichka.backend_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiResponse(
        @JsonProperty("response_text")
        String responseText) {
}
