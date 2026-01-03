package com.morzevichka.backend_api.infrastructure.redis.service;

import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import com.morzevichka.backend_api.infrastructure.redis.repository.CacheRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CachePlaceService {

    private final CacheRouteRepository repository;

    public Optional<RouteCache> findById(UUID id) {
        return repository.findById(id);
    }

    public void save(RouteCache cache) {
        cache.setTtl(30*24*60*60L);
        repository.save(cache);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
