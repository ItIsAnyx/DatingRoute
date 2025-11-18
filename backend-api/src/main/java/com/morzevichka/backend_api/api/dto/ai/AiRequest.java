package com.morzevichka.backend_api.api.dto.ai;

import com.morzevichka.backend_api.domain.value.InnerContext;

import java.util.List;

public record AiRequest(String message, List<InnerContext> context) {}
