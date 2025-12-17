package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.exception.route.RouteNotFoundException;
import com.morzevichka.backend_api.infrastructure.jpa.entity.RouteEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaRouteMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;


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
    public Route findByChatIdWithPoints(Long chatId) {
        return mapper.toDomain(jpa.findByChatIdWithPoints(chatId).orElseThrow(() -> new RouteNotFoundException(chatId)));
    }

    @Override
    public Route findByIdWithPoints(UUID routeId) {
        return mapper.toDomain(jpa.findByIdWithPoints(routeId).orElseThrow(() -> new RouteNotFoundException(routeId)));
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return jpa.existsByChatId(chatId);
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
