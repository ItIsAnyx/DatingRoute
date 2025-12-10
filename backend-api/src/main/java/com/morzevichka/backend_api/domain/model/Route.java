package com.morzevichka.backend_api.domain.model;

import com.morzevichka.backend_api.domain.value.RouteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    private UUID id;
    private Chat chat;
    private LocalDateTime updatedAt;
    private RouteType routeType = RouteType.PEDESTRIAN;
    private List<RoutePoint> points = new ArrayList<>();
}
