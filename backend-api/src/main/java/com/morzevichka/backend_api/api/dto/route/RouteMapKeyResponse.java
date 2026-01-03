package com.morzevichka.backend_api.api.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RouteMapKeyResponse {
    private String key;
}
