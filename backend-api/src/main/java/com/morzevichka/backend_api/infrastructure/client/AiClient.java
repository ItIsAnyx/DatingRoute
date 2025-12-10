package com.morzevichka.backend_api.infrastructure.client;

import com.morzevichka.backend_api.application.dto.ai.*;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.value.InnerContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final AiClientProperties properties;

    private WebClient webClient;

    @PostConstruct
    void setWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeader("AI_SECRET_KEY", properties.getSecretKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AiCreateClientResponse createChatRequest(String prompt) {
        AiCreateClientRequest request = new AiCreateClientRequest(prompt);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response/create")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiCreateClientResponse>() {})
                .block();
    }

    public AiClientResponse sendMessageRequest(String prompt, Context context) {
        AiClientRequest request = new AiClientRequest(prompt, context.getInnerContexts());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiClientResponse>() {})
                .block();
    }

    public AiPointsSummaryResponse summarizeRequest(List<InnerContext> context) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response/summarize")
                        .build()
                )
                .bodyValue(Map.of("context", context))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiPointsSummaryResponse>() {})
                .block();
    }
}
