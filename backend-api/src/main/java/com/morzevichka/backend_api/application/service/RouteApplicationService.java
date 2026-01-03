package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.client.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteApplicationService {

    private final RouteRepository routeRepository;

    public Route getRoute(Long chatId) {
        return routeRepository.findByChatIdWithPoints(chatId);
    }

    public Route getRouteById(UUID routeId) {
        return routeRepository.findByIdWithPoints(routeId);
    }

    public boolean existsByChatId(Long chatId) {
        return routeRepository.existsByChatId(chatId);
    }

    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }
}
