package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.AuthenticationResponse;
import com.morzevichka.backend_api.dto.LoginRequest;
import com.morzevichka.backend_api.dto.RegisterRequest;
import com.morzevichka.backend_api.entity.Role;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.repository.UserRepository;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerBody) {
        User user = User.builder()
                .email(registerBody.email())
                .login(registerBody.login())
                .password(passwordEncoder.encode(registerBody.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtProvider.generateToken(new CustomUserDetails(user));
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest loginBody) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBody.email(),
                        loginBody.password()
                )
        );

        User user = userRepository.findByEmail(loginBody.email())
                .orElseThrow();

        String token = jwtProvider.generateToken(new CustomUserDetails(user));

        return new AuthenticationResponse(token);
    }
}
