package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.request.LoginRequest;
import com.morzevichka.backend_api.dto.request.RegisterRequest;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerBody) {
        User user = userService.createUser(
                registerBody.login(),
                registerBody.email(),
                registerBody.password()
        );

        return jwtProvider.generateToken(new CustomUserDetails(user));
    }

    public String login(LoginRequest loginBody) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBody.email(),
                        loginBody.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return jwtProvider.generateToken(userDetails);
    }
}
