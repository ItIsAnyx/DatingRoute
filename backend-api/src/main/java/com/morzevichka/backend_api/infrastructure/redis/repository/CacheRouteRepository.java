package com.morzevichka.backend_api.infrastructure.redis.repository;

import com.morzevichka.backend_api.infrastructure.redis.cachedto.RouteCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CacheRouteRepository extends CrudRepository<RouteCache, UUID> {

    Optional<RouteCache> findById(UUID id);

}
