package com.morzevichka.backend_api.security;

import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.domain.repository.UserRepository;
import com.morzevichka.backend_api.infrastructure.jpa.JpaUserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(username);
        return new CustomUserDetails(user);
    }
}
