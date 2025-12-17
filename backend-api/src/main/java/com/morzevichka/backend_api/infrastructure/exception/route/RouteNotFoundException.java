package com.morzevichka.backend_api.infrastructure.exception.route;

import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;

import java.util.UUID;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException(Long chatId) {
        super("Route with chat id: " + chatId + " not found");
    }

    public RouteNotFoundException(UUID routeId) {
        super("Route with id: " + routeId + " not found");
    }
}
