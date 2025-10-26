package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.authentication.*;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.CustomUserDetailsService;
import com.morzevichka.backend_api.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Cacheable(value = "users", key = "#registerBody.email().toLowerCase()")
    public User register(RegisterRequest registerBody) {

        return userService.createUser(
                registerBody.login(),
                registerBody.email(),
                registerBody.password()
        );
    }

    public User login(LoginRequest loginBody) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBody.email(),
                        loginBody.password()
                )
        );

        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    public String refresh(RefreshRequest request) {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtProvider.extractUsername(request.refreshToken()));
        return jwtProvider.generateToken(userDetails);
    }
}
