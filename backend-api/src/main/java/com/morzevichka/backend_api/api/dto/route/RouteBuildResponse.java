package com.morzevichka.backend_api.api.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class RouteBuildResponse {
    @JsonProperty(value = "route_id")
    private UUID routeId;
    private List<PointDto> points;
    private Map<String, String> params;
}
