package com.morzevichka.backend_api.infrastructure.repository;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.UserRepository;
import com.morzevichka.backend_api.infrastructure.jpa.JpaUserRepositoryAdapter;
import com.morzevichka.backend_api.infrastructure.redis.service.CacheUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class JpaCacheUserRepositoryAdapter implements UserRepository {

    private final CacheUserService cacheService;
    private final JpaUserRepositoryAdapter jpaRepository;

    @Override
    public User findByEmail(String email) {
        Optional<User> cacheUser = cacheService.findByEmail(email);

        return cacheUser.orElseGet(() -> {
            User user = jpaRepository.findByEmail(email);
            cacheService.save(user);
            return user;
        });
    }

    @Override
    public User save(User user) {
        User saved = jpaRepository.save(user);
        cacheService.save(saved);
        return saved;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
