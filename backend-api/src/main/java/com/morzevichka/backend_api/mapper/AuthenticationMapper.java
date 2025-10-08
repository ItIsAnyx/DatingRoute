package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.authentication.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse toDto(String token) {
        return new AuthenticationResponse(token);
    }
}
