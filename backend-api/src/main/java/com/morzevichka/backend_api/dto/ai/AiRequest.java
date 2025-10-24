package com.morzevichka.backend_api.dto.ai;

import java.util.List;
import com.morzevichka.backend_api.entity.ContextJson;

public record AiRequest(String message, List<ContextJson> context) {}