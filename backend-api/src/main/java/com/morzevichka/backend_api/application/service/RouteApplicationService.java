package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.api.dto.route.RoutePlacesResponse;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.client.AiClient;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteApplicationService {

    private final AiClient aiClient;
    private final RouteRepository routeRepository;

    public RoutePlacesResponse getPlaces(Context context) {
        return aiClient.summarizeRequest(context.getInnerContexts());
    }

    public Route getPlaces(Long chatId) {
        return routeRepository.findByChatId(chatId);
    }

    public Route updatePlaces(Long chatId, List<String> places) {
        Route route = routeRepository.findByChatId(chatId);

        route.setPlaces(places);
        return routeRepository.save(route);
    }
}
