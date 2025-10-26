package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.authentication.*;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.AuthenticationMapper;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.JwtProvider;
import com.morzevichka.backend_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationMapper authenticationMapper;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        User user = authenticationService.register(request);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return ResponseEntity.ok(
                authenticationMapper.toDto(
                        jwtProvider.generateToken(userDetails),
                        jwtProvider.generateRefreshToken(userDetails)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        User user = authenticationService.login(request);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return ResponseEntity.ok(
                authenticationMapper.toDto(
                        jwtProvider.generateToken(userDetails),
                        jwtProvider.generateRefreshToken(userDetails)
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        String refreshToken = authenticationService.refresh(request);
        return ResponseEntity.ok(authenticationMapper.toRefreshDto(refreshToken));
    }
}
