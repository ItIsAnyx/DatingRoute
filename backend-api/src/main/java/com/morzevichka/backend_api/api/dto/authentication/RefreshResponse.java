package com.morzevichka.backend_api.api.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RefreshResponse {
    @JsonProperty("access_token")
    String accessToken;
}
