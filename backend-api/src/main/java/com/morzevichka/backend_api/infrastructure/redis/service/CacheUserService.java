package com.morzevichka.backend_api.infrastructure.redis.service;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.infrastructure.redis.cachedto.UserCache;
import com.morzevichka.backend_api.infrastructure.redis.mapper.CacheUserMapper;
import com.morzevichka.backend_api.infrastructure.redis.repository.CacheUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheUserService {

    private final CacheUserRepository repository;
    private final CacheUserMapper mapper;

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    public User save(User user) {
        UserCache cache = mapper.toCache(user);
        return mapper.toDomain(repository.save(cache));
    }

    public void delete(User user) {
        repository.delete(mapper.toCache(user));
    }
}
