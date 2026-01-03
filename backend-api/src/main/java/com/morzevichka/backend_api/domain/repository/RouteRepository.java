package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.Route;

import java.util.UUID;

public interface RouteRepository {

    Route findByChatId(Long chatId);

    Route findByChatIdWithPoints(Long chatId);

    Route findByIdWithPoints(UUID routeId);

    boolean existsByChatId(Long chatId);

    Route save(Route route);

    void delete(Route route);

    void deleteByChatId(Long chatId);
}
