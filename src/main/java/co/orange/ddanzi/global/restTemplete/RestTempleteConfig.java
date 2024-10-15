package co.orange.ddanzi.global.restTemplete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

    public class LoggingInterceptor implements ClientHttpRequestInterceptor {
        private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

        @Override
        public ClientHttpResponse intercept(
                org.springframework.http.HttpRequest request,
                byte[] body,
                org.springframework.http.client.ClientHttpRequestExecution execution) throws IOException {

            log.info("Request URI: {}", request.getURI());
            log.info("Request Method: {}", request.getMethod());
            log.info("Request Headers: {}", request.getHeaders());
            log.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));

            ClientHttpResponse response = execution.execute(request, body);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            log.info("Response Status Code: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());
            log.info("Response Body: {}", responseBody.toString());

            return response;
        }
    }
}
