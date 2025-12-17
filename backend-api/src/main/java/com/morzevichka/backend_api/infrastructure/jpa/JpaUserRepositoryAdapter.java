package com.morzevichka.backend_api.infrastructure.jpa;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.UserRepository;
import com.morzevichka.backend_api.infrastructure.exception.user.UserNotFoundException;
import com.morzevichka.backend_api.infrastructure.jpa.entity.UserEntity;
import com.morzevichka.backend_api.infrastructure.jpa.mapper.JpaUserMapper;
import com.morzevichka.backend_api.infrastructure.jpa.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpa;
    private final JpaUserMapper jpaUserMapper;

    public User findByEmail(String email) {
        return jpaUserMapper.toDomain(
                jpa.getByEmail(email).orElseThrow(() -> new UserNotFoundException(email))
        );
    }

    public User save(User user) {
        UserEntity entity = jpaUserMapper.toEntity(user);
        return jpaUserMapper.toDomain(jpa.save(entity));
    }

    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }
}
