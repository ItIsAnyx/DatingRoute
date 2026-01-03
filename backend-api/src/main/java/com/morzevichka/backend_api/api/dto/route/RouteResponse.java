package com.morzevichka.backend_api.api.dto.route;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class RouteResponse {
    private boolean exists;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "route_id")
    private UUID routeId;
}
