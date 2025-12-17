package com.morzevichka.backend_api.domain.repository;

import com.morzevichka.backend_api.domain.model.User;

public interface UserRepository {

    User findByEmail(String email);

    User save(User user);

    boolean existsByEmail(String email);
}
