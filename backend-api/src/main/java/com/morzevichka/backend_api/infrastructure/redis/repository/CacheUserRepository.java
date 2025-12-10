package com.morzevichka.backend_api.infrastructure.redis.repository;

import com.morzevichka.backend_api.infrastructure.redis.cachedto.UserCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CacheUserRepository extends CrudRepository<UserCache, Long> {
    Optional<UserCache> findByEmail(String email);

    boolean existsByEmail(String email);
}
