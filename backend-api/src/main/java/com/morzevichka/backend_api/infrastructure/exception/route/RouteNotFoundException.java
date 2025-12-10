package com.morzevichka.backend_api.infrastructure.exception.route;

import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException(Long chatId) {
        super("Route with chat id: " + chatId + " not found");
    }
}
