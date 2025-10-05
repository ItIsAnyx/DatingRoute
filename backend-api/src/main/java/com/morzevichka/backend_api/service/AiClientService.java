package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.request.AiRequest;
import com.morzevichka.backend_api.dto.response.AiChatCreateResponse;
import com.morzevichka.backend_api.dto.response.AiResponse;
import com.morzevichka.backend_api.exception.AiServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AiClientService {

    private final RestTemplate restTemplate;

    @Value("${ai-service-url}")
    private String aiServiceUrl;

    public String getAiResponse(String prompt) {
        String fullUrl = aiServiceUrl + "/api/ai/response";

        AiRequest request = new AiRequest(prompt);

//        ResponseEntity<AiResponse> response = restTemplate.postForEntity(fullUrl, request, AiResponse.class);

//        if (Objects.isNull(response.getBody())) {
//            throw new AiServiceException("Ai service returned empty body");
//        }

//        return response.getBody().responseText();

        return "Text";
    }

    public AiChatCreateResponse getAiChatCreateResponse(String prompt) {
        String fullUrl = aiServiceUrl + "/api/ai/response/create";

        AiRequest request = new AiRequest(prompt);

//        ResponseEntity<AiChatCreateResponse> response = restTemplate.postForEntity(fullUrl, request, AiChatCreateResponse.class);
//
//        if (Objects.isNull(response.getBody())) {
//            throw new AiServiceException("Ai service returned empty body");
//        }
//
//        return response.getBody();

        return new AiChatCreateResponse("Title", "Text");
    }
}
