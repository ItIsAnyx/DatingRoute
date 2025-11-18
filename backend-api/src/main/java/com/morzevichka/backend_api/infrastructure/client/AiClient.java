package com.morzevichka.backend_api.infrastructure.client;

import com.morzevichka.backend_api.api.dto.ai.AiCreateRequest;
import com.morzevichka.backend_api.api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.api.dto.ai.AiRequest;
import com.morzevichka.backend_api.api.dto.ai.AiResponse;
import com.morzevichka.backend_api.domain.model.Context;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


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

    public AiCreateResponse createChatRequest(String prompt) {
        AiCreateRequest request = new AiCreateRequest(prompt);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response/create")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiCreateResponse>() {})
                .block();
    }

    public AiResponse sendMessageRequest(String prompt, Context context) {
        AiRequest request = new AiRequest(prompt, context.getInnerContexts());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/response")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AiResponse>() {})
                .block();
    }

}
