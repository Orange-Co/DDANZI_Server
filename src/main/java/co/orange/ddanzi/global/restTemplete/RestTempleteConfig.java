package co.orange.ddanzi.global.restTemplete;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTempleteConfig {

    @Value("${ddanzi.server.host}")
    private String hostUrl;

    @Value("${ddanzi.server.port}")
    private int port;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(hostUrl+":"+port)
                .build();
    }
}
