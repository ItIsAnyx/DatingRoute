package com.morzevichka.backend_api.dto.error;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ValidationErrorResponse extends DefaultErrorResponse {
    private Map<String, String> details;
}
