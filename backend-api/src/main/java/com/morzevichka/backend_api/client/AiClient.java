package com.morzevichka.backend_api.client;

import com.morzevichka.backend_api.dto.ai.AiCreateRequest;
import com.morzevichka.backend_api.dto.ai.AiRequest;
import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.dto.ai.AiResponse;
import com.morzevichka.backend_api.entity.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final AiClientProperties properties;
    private final RestTemplate restTemplate;

    public AiCreateResponse createChatRequest(String prompt) {
        AiCreateRequest request = new AiCreateRequest(prompt);

        return restTemplate.postForEntity(
                properties.getUrl() + "/api/response/create",
                createEntity(request),
                AiCreateResponse.class
        ).getBody();
    }

    public AiResponse sendMessageRequest(String prompt, Context context) {
        AiRequest request = new AiRequest(prompt, context.getContext());

        return restTemplate.postForEntity(
                properties.getUrl() + "/api/response",
                createEntity(request),
                AiResponse.class
        ).getBody();
    }

    public <T> HttpEntity<T> createEntity(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("AI_SECRET_KEY", properties.getSecretKey());

        return new HttpEntity<>(request, headers);
    }
}
