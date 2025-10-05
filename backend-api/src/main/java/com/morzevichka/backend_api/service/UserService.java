package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.entity.Role;
import com.morzevichka.backend_api.exception.UserAlreadyExistsException;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.repository.UserRepository;
import com.morzevichka.backend_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(String login, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        User user = User.builder()
                .login(login)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}