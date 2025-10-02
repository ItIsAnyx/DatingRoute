package com.morzevichka.backend_api.controller;

import com.morzevichka.backend_api.dto.AuthenticationResponse;
import com.morzevichka.backend_api.dto.LoginRequest;
import com.morzevichka.backend_api.dto.RegisterRequest;
import com.morzevichka.backend_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerBody) {
        return ResponseEntity.ok(authService.register(registerBody));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginBody) {
        return ResponseEntity.ok(authService.login(loginBody));
    }
}
