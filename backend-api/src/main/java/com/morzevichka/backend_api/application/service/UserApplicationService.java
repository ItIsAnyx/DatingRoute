package com.morzevichka.backend_api.application.service;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.UserRepository;
import com.morzevichka.backend_api.domain.service.UserService;
import com.morzevichka.backend_api.domain.value.Role;
import com.morzevichka.backend_api.infrastructure.exception.user.UserNotFoundException;
import com.morzevichka.backend_api.infrastructure.exception.user.UserNotFoundInContextException;
import com.morzevichka.backend_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserService userService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UserNotFoundInContextException();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        } else {
            throw new UserNotFoundInContextException();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String login, String email, String password) {
        userService.ensureUserDoesNotExists(email);

        User user = User.builder()
                .login(login)
                .email(email)
                .passwordHash(password)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }
}
