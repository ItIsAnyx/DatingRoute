package com.morzevichka.backend_api.api.dto.ai;

import com.morzevichka.backend_api.domain.value.InnerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class AiCreateResponse {
    private String title;
    private String message;
    private List<InnerContext> context;
}
