package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.authentication.*;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.User;
import com.morzevichka.backend_api.security.CustomUserDetailsService;
import com.morzevichka.backend_api.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserApplicationService userApplicationService;

    public AuthenticationResponse register(RegisterRequest request) {
        String passwordHash = passwordEncoder.encode(request.password());

        User user = userApplicationService.createUser(request.login(), request.email(), passwordHash);

        return new AuthenticationResponse(
                jwtProvider.generateToken(user.getEmail()),
                jwtProvider.generateRefreshToken(user.getEmail())
        );
    }

    public AuthenticationResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new AuthenticationResponse(
                jwtProvider.generateToken(userDetails.getUsername()),
                jwtProvider.generateRefreshToken(userDetails.getUsername())
        );
    }

    public RefreshResponse refresh(RefreshRequest refresh) {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtProvider.extractUsername(refresh.refreshToken()));

        return new RefreshResponse(
                jwtProvider.generateToken(userDetails.getUsername())
        );
    }
}
