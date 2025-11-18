package com.morzevichka.backend_api.domain.service;

import com.morzevichka.backend_api.domain.repository.UserRepository;
import com.morzevichka.backend_api.infrastructure.exception.user.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void ensureUserDoesNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
    }
}
