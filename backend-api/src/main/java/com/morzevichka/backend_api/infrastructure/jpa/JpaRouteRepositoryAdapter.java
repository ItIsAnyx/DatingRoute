package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.exception.route.RouteNotFoundException;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaRouteMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class JpaRouteRepositoryAdapter implements RouteRepository {

    private final JpaRouteRepository jpa;
    private final JpaRouteMapper mapper;

    @Override
    public Route findByChatId(Long chatId) {
        return mapper.toDomain(jpa.findByChatId(chatId).orElseThrow(() -> new RouteNotFoundException(chatId)));
    }

    @Override
    public Route save(Route route) {
        RouteEntity entity = mapper.toEntity(route);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void delete(Route route) {
        jpa.delete(mapper.toEntity(route));
    }

    @Override
    public void deleteByChatId(Long chatId) {
        jpa.deleteByChatId(chatId);
    }
}
