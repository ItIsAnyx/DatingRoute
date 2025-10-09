package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.authentication.AuthenticationResponse;
import com.morzevichka.backend_api.dto.authentication.RefreshResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse toDto(String token, String refreshToken) {
        return new AuthenticationResponse(token, refreshToken);
    }

    public RefreshResponse toRefreshDto(String token) {
        return new RefreshResponse(token);
    }
}
