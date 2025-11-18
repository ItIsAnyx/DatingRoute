package com.morzevichka.backend_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        int status;
        String message;

        if (authException.getCause() instanceof JwtException) {
            status = HttpStatus.BAD_REQUEST.value();
            message = authException.getMessage();
        } else if (authException.getCause() instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED.value();
            message = authException.getMessage();
        } else {
            status = HttpStatus.UNAUTHORIZED.value();
            message = authException.getMessage();
        }

        response.setStatus(status);

        DefaultErrorResponse errorResponse = DefaultErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(HttpStatus.valueOf(status).getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        String body = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }
}
