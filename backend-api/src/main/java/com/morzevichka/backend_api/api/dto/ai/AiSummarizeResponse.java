package com.morzevichka.backend_api.api.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class AiSummarizeResponse {
    private List<String> points = new ArrayList<>();
}
