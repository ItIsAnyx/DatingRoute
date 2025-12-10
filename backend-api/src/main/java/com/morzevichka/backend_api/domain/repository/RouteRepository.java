package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.Route;

public interface RouteRepository {

    Route findByChatId(Long chatId);

    Route save(Route route);

    void delete(Route route);

    void deleteByChatId(Long chatId);
}
