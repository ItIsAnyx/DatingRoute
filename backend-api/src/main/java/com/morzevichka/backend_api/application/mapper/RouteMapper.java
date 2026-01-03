package com.morzevichka.backend_api.application.mapper;

import com.morzevichka.backend_api.api.dto.route.PointDto;
import com.morzevichka.backend_api.api.dto.route.RouteBuildResponse;
import com.morzevichka.backend_api.api.dto.route.RoutePointsResponse;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.model.RoutePoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RouteMapper {

    public RoutePointsResponse toRoutePoints(Route route) {
        return RoutePointsResponse.builder()
                .routeId(route.getId())
                .points(route.getPoints().stream().map(RoutePoint::getName).toList())
                .build();
    }

    public RouteBuildResponse toRouteBuild(Route route, Map<String, String> params) {
        List<PointDto> points = route.getPoints().stream()
                .filter(routePoint -> Objects.nonNull(routePoint.getLat()) && Objects.nonNull(routePoint.getLon()))
                .map(routePoint -> new PointDto(routePoint.getName(), List.of(routePoint.getLat(), routePoint.getLon())))
                .toList();

        return new RouteBuildResponse(route.getId(), points, params);
    }
}
