package com.morzevichka.backend_api.application.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class AiPointsSummaryResponse {
    private List<String> points;
}
