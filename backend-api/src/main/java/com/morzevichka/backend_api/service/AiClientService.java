package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.ai.AiRequest;
import com.morzevichka.backend_api.dto.ai.AiCreateResponse;
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
    private String secretKey;

    private final RestTemplate restTemplate;

    public AiCreateResponse createChat(String prompt) {
        AiRequest requestBody = new AiRequest(prompt);

        return restTemplate.postForEntity(
                aiServiceUrl + "/api/response",
                createEntity(requestBody),
                AiCreateResponse.class
        ).getBody();
    }

    public <T> HttpEntity<T> createEntity(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("x-api-key", secretKey);

        return new HttpEntity<>(request, headers);
    }
}
