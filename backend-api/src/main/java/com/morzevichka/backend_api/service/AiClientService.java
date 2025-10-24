package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.ai.AiCreateRequest;
import com.morzevichka.backend_api.dto.ai.AiRequest;
import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
import com.morzevichka.backend_api.dto.ai.AiResponse;
import com.morzevichka.backend_api.entity.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiClientService {

    @Value("${service.ai.url}")
    private String aiServiceUrl;

    @Value("${service.ai.secret-key}")
    private String aiSecretKey;

    private final RestTemplate restTemplate;

    public AiCreateResponse createChat(String prompt) {
        AiCreateRequest request = new AiCreateRequest(prompt);

        return restTemplate.postForEntity(
                aiServiceUrl + "/api/response/create",
                createEntity(request),
                AiCreateResponse.class
        ).getBody();
    }

    public AiResponse sendMessage(String prompt, Context context) {
        AiRequest request = new AiRequest(prompt, context.getContext());

        return restTemplate.postForEntity(
                aiServiceUrl + "/api/response",
                createEntity(request),
                AiResponse.class
        ).getBody();
    }

    public <T> HttpEntity<T> createEntity(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("AI_SECRET_KEY", aiSecretKey);

        return new HttpEntity<>(request, headers);
    }
}
