package com.morzevichka.backend_api.infrastructure.client;

import com.morzevichka.backend_api.api.dto.ai.*;
import com.morzevichka.backend_api.domain.model.Context;
import com.morzevichka.backend_api.domain.value.InnerContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
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

    public AiCreateResponse createChatRequest(String prompt, Boolean test) {
        AiCreateRequest request = new AiCreateRequest(prompt);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response/create")
                        .queryParam("test", test)
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiCreateResponse>() {})
                .block();
    }

    public AiResponse sendMessageRequest(String prompt, Context context, Boolean test) {
        AiRequest request = new AiRequest(prompt, context.getInnerContexts());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response")
                        .queryParam("test", test)
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiResponse>() {})
                .block();
    }

    public AiSummarizeResponse summarizeRequest(List<InnerContext> context) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response/summarize")
                        .build()
                )
                .bodyValue(Map.of("context", context))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiSummarizeResponse>() {})
                .block();
    }
}
