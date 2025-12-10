package com.morzevichka.backend_api.application.dto.ai;

import com.morzevichka.backend_api.domain.value.InnerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class AiClientResponse {
    private String message;
    private List<InnerContext> context;
}
