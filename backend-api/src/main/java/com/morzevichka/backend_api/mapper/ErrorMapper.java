package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.dto.error.ValidationErrorResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ErrorMapper {
    public ValidationErrorResponse toValidationDto(DefaultErrorResponse response, Map<String, String> details) {
        return ValidationErrorResponse.builder()
                .timestamp(response.getTimestamp())
                .status(response.getStatus())
                .error(response.getError())
                .message(response.getMessage())
                .path(response.getPath())
                .details(details)
                .build();
    }
}
