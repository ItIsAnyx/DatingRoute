package com.morzevichka.backend_api.infrastructure.redis.service;

import com.morzevichka.backend_api.domain.model.Route;
import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import com.morzevichka.backend_api.infrastructure.redis.mapper.CacheRouteMapper;
import com.morzevichka.backend_api.infrastructure.redis.repository.CacheRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheRouteService {

    private final CacheRouteMapper mapper;
    private final CacheRouteRepository repository;

    public Optional<Route> findByChatId(Long chatId) {
        return repository.findByChatId(chatId)
                .map(mapper::toDomain);
    }

    public Route save(Route route) {
        RouteCache cache = mapper.toCache(route);
        return mapper.toDomain(repository.save(cache));
    }

    public void delete(Route route) {
        repository.delete(mapper.toCache(route));
    }

    public void deleteByChatId(Long chatId) {
        repository.deleteByChatId(chatId);
    }
}
