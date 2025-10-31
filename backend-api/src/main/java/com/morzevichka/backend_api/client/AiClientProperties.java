package com.morzevichka.backend_api.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "service.ai")
public class AiClientProperties {
    private String url;
    private String secretKey;
}
