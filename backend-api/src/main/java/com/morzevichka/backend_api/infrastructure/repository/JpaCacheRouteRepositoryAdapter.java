package com.morzevichka.backend_api.infrastructure.repository;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.domain.model.RoutePoint;
import com.morzevichka.backend_api.domain.repository.RouteRepository;
import com.morzevichka.backend_api.domain.value.Place;
import com.morzevichka.backend_api.infrastructure.jpa.JpaRouteRepositoryAdapter;
import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import com.morzevichka.backend_api.infrastructure.redis.service.CachePlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Primary
@Repository
@RequiredArgsConstructor
public class JpaCacheRouteRepositoryAdapter implements RouteRepository {

    private final CachePlaceService cacheService;
    private final JpaRouteRepositoryAdapter jpaRepository;

    @Override
    public Route findByChatId(Long chatId) {
        return jpaRepository.findByChatId(chatId);
    }

    @Override
    public Route findByChatIdWithPoints(Long chatId) {
        Route route = jpaRepository.findByChatIdWithPoints(chatId);

        getPointsFromCache(route);
        return route;
    }

    @Override
    public Route findByIdWithPoints(UUID routeId) {
        Route route = jpaRepository.findByIdWithPoints(routeId);

        getPointsFromCache(route);
        return route;
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return jpaRepository.existsByChatId(chatId);
    }

    @Override
    public Route save(Route route) {
        Route saved = jpaRepository.save(route);

        RouteCache cache = cacheService.findById(saved.getId())
                .orElse(new RouteCache(saved.getId(), new HashMap<>()));
        Map<UUID, Place> cachePlaces = cache.getPlaces();

        for (RoutePoint point : route.getPoints()) {
            if (Objects.nonNull(point.getAddress()) && Objects.nonNull(point.getLat()) && Objects.nonNull(point.getLon())) {
                cachePlaces.put(point.getId(),
                        Place.builder()
                                .address(point.getAddress())
                                .lat(point.getLat())
                                .lon(point.getLon())
                                .build()
                );
            }
        }

        if (!cachePlaces.isEmpty()) cacheService.save(cache);
        return saved;
    }

    @Override
    public void delete(Route route) {
        if (route != null) {
            jpaRepository.delete(route);
            cacheService.deleteById(route.getId());
        }
    }

    @Override
    public void deleteByChatId(Long chatId) {
        Route route = jpaRepository.findByChatId(chatId);
        jpaRepository.deleteByChatId(chatId);
        cacheService.deleteById(route.getId());
    }

    private void getPointsFromCache(Route route) {
        cacheService.findById(route.getId())
                .ifPresent(routeCache -> {
                    Map<UUID, Place> cachePlaces = routeCache.getPlaces();

                    route.getPoints().forEach(routePoint ->
                            Optional.ofNullable(cachePlaces.get(routePoint.getId()))
                                    .ifPresent(cache -> {
                                        routePoint.setAddress(cache.getAddress());
                                        routePoint.setLat(cache.getLat());
                                        routePoint.setLon(cache.getLon());
                                    }));
                });
    }
}
