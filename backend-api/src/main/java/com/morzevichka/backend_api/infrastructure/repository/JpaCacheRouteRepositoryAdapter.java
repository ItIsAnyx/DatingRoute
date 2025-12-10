package com.morzevichka.backend_api.infrastructure.repository;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.infrastructure.jpa.JpaRouteRepositoryAdapter;
import com.morzevichka.backend_api.infrastructure.redis.service.CacheRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class JpaCacheRouteRepositoryAdapter implements RouteRepository {

    private final CacheRouteService cacheService;
    private final JpaRouteRepositoryAdapter jpaRepository;

    @Override
    public Route findByChatId(Long chatId) {
        Optional<Route> cacheRoute = cacheService.findByChatId(chatId);

        return cacheRoute.orElseGet(() -> {
            Route route = jpaRepository.findByChatId(chatId);
            cacheService.save(route);
            return route;
        });
    }

    @Override
    public Route save(Route route) {
        route.setUpdatedAt(LocalDateTime.now());
        Route saved = jpaRepository.save(route);
        cacheService.save(saved);
        return saved;
    }

    @Override
    public void delete(Route route) {
        jpaRepository.delete(route);
        cacheService.delete(route);
    }

    @Override
    public void deleteByChatId(Long chatId) {
        jpaRepository.deleteByChatId(chatId);
        cacheService.deleteByChatId(chatId);
    }
}
