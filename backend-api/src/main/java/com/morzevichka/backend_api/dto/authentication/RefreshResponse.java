package com.morzevichka.backend_api.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshResponse(
        @JsonProperty("access_token")
        String token
) {}
