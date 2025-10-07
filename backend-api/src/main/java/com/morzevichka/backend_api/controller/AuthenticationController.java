package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.response.AuthenticationResponse;
import com.morzevichka.backend_api.dto.request.LoginRequest;
import com.morzevichka.backend_api.dto.request.RegisterRequest;
import com.morzevichka.backend_api.mapper.AuthenticationMapper;
import com.morzevichka.backend_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest registerBody) {
        AuthenticationResponse response = authenticationMapper.toDto(authenticationService.register(registerBody));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginBody) {
        AuthenticationResponse response = authenticationMapper.toDto(authenticationService.login(loginBody));
        return ResponseEntity.ok(response);
    }
}
