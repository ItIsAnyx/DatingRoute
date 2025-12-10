package com.morzevichka.backend_api.infrastructure.redis.repository;

import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CacheRouteRepository extends CrudRepository<RouteCache, Long> {
    Optional<RouteCache> findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
