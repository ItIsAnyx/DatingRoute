package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.route.RouteBuildResponse;
import com.morzevichka.backend_api.api.dto.route.RouteMapKeyResponse;
import com.morzevichka.backend_api.api.dto.route.RoutePointsResponse;
import com.morzevichka.backend_api.api.dto.route.RouteResponse;
import com.morzevichka.backend_api.application.dto.route.RouteAddressClientResponse;
import com.morzevichka.backend_api.application.dto.route.RouteCoordsClientResponse;
import com.morzevichka.backend_api.application.mapper.RouteMapper;
import com.morzevichka.backend_api.application.service.ChatApplicationService;
import com.morzevichka.backend_api.application.service.ContextApplicationService;
import com.morzevichka.backend_api.application.service.RouteApplicationService;
import com.morzevichka.backend_api.domain.model.Chat;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.model.RoutePoint;
import com.morzevichka.backend_api.domain.value.RouteType;
import com.morzevichka.backend_api.infrastructure.client.AiClient;
import com.morzevichka.backend_api.infrastructure.client.RouteClient;
import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class RouteUseCase {

    private final ContextApplicationService contextApplicationService;
    private final RouteApplicationService routeApplicationService;
    private final AiClient aiClient;
    private final RouteMapper routeMapper;
    private final ChatApplicationService chatApplicationService;
    private final RouteClient routeClient;

    public RouteResponse getRoute(Long chatId) {
        try {
            Route route = routeApplicationService.getRoute(chatId);
            return RouteResponse.builder()
                    .exists(true)
                    .routeId(route.getId())
                    .build();
        } catch (NotFoundException e) {
            return RouteResponse.builder().exists(false).build();
        }
    }

    @Transactional
    public RoutePointsResponse create(Long chatId) {
        Chat chat = chatApplicationService.getChatForCurrentUser(chatId);

        if (routeApplicationService.existsByChatId(chat.getId())) {
            Route route = routeApplicationService.getRoute(chatId);
            return routeMapper.toRoutePoints(route);
        }

        Route route = new Route();
        route.setChat(chat);

        Context context = contextApplicationService.getContext(chat.getId());

        List<String> pointsFromAi = aiClient.summarizeRequest(context.getInnerContexts()).getPoints();
        List<RoutePoint> routePoints = fillRoutePoints(pointsFromAi);

        route.setPoints(routePoints);
        Route saved = routeApplicationService.saveRoute(route);

        return routeMapper.toRoutePoints(saved);
    }

    public RoutePointsResponse getRoute(UUID routeId) {
        return routeMapper.toRoutePoints(routeApplicationService.getRouteById(routeId));
    }

    public RoutePointsResponse updatePlaces(UUID routeId, List<String> places) {
        Route route = routeApplicationService.getRouteById(routeId);

        List<RoutePoint> routePoints = fillRoutePoints(places);

        route.setPoints(routePoints);
        Route saved = routeApplicationService.saveRoute(route);

        return routeMapper.toRoutePoints(saved);
    }

    public RouteBuildResponse build(UUID routeId) {
        Route route = routeApplicationService.getRouteById(routeId);

        Map<String, String> params = Map.of("routingMode", RouteType.PEDESTRIAN.toString().toLowerCase());

        for (RoutePoint point : route.getPoints()) {
            if (point.getAddress() == null && point.getName() != null) {
                String address = fetchAddress(point.getName());
                point.setAddress(address);
            }

            if (point.getLat() == null || point.getLon() == null) {
                Pair<Double, Double> coords = fetchCoords(point.getName(), point.getAddress());
                point.setLat(coords.getFirst());
                point.setLon(coords.getSecond());
            }
        }

        routeApplicationService.saveRoute(route);

        return routeMapper.toRouteBuild(route, params);
    }


    private List<RoutePoint> fillRoutePoints(List<String> points) {
        List<RoutePoint> routePoints = new ArrayList<>();
        int order = 0;
        for (String point : points) {
            RoutePoint routePoint = new RoutePoint();
            routePoint.setOrderIndex(order++);
            routePoint.setName(point);
            routePoints.add(routePoint);
        }

        return routePoints;
    }

    private String fetchAddress(String name) {
        try {
            RouteAddressClientResponse response = routeClient.getAddress(name);
            System.out.printf("%s %s\n", response.getName(), response.getAddress());
            return response.getAddress();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить адрес для точки: " + name, e);
        }
    }

    private Pair<Double, Double> fetchCoords(String name, String address) {
        try {
            RouteCoordsClientResponse response = routeClient.getCoords(name, address);
            System.out.printf("%s %s %s\n", response.getName(), response.getAddress(), response.getCoords());
            return Pair.of(response.getCoords().getFirst(), response.getCoords().getLast());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить координаты для точки: " + name, e);
        }
    }

    public RouteMapKeyResponse getMapKey() {
        return routeClient.getMapKey();
    }
}
