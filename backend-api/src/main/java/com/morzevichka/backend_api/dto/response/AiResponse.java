package com.morzevichka.backend_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiResponse(
        @JsonProperty("message")
        String message,
        List<Integer> tensor,
        @JsonProperty("tensor_shape")
        List<Integer> tensorShape) {
}
