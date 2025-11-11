package com.pulsehub.stocksservice.stocks_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class AiClient {

    private final WebClient webClient;

    public AiClient(@Value("${ai.service-url}") String aiUrl) {
        this.webClient = WebClient.builder().baseUrl(aiUrl).build();
    }

    public String getStockInsight(String question, String context) {
        try {
            Map<String, String> body = Map.of(
                    "question", question,
                    "context", context != null ? context : ""
            );

            Map<?, ?> response = webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null && response.get("response") != null
                    ? response.get("response").toString()
                    : "AI: No insight available.";
        } catch (Exception e) {
            return "AI: Service unavailable (" + e.getMessage() + ")";
        }
    }
}
