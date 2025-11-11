package com.pulsehub.stocksservice.stocks_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsehub.stocksservice.stocks_service.model.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StocksAiService {

    @Value("${ai.service-url}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // Cache to store the last AI result for tickers
    private final Map<String, String> aiCache = new ConcurrentHashMap<>();

    /**
     * Get AI recommendations for a list of stocks.
     * Uses a single AI request to minimize API load.
     */
    public Map<String, String> getAiRecommendations(List<Stock> stocks) {
        Map<String, String> recommendations = new HashMap<>();
        try {
            // Prepare prompt
            StringBuilder prompt = new StringBuilder("Provide 1–2 line investment advice for these stocks:\n");
            for (Stock s : stocks) {
                prompt.append(String.format("- %s: price %.2f (%s)\n", s.getTicker(), s.getPrice(), s.getChange()));
            }

            // Check cache first
            String cacheKey = Integer.toHexString(prompt.toString().hashCode());
            if (aiCache.containsKey(cacheKey)) {
                String cachedResponse = aiCache.get(cacheKey);
                return parseAiResponse(cachedResponse, stocks);
            }

            // Create request body for AI service
            Map<String, String> req = Map.of("question", prompt.toString());
            String response = restTemplate.postForObject(aiServiceUrl, req, String.class);

            if (response == null || response.isBlank()) {
                for (Stock s : stocks) {
                    recommendations.put(s.getTicker(),
                            "AI Suggestion: Data unavailable. Try again later ⏳");
                }
                return recommendations;
            }

            aiCache.put(cacheKey, response);
            recommendations = parseAiResponse(response, stocks);

        } catch (Exception e) {
            System.err.println("⚠️ AI Fetch Error: " + e.getMessage());
            // graceful fallback
            for (Stock s : stocks) {
                recommendations.put(s.getTicker(),
                        "AI Suggestion: Diversify holdings, monitor trends closely 📊");
            }
        }
        return recommendations;
    }

    /**
     * Parse AI response — tries to map ticker-based suggestions from the text.
     */
    private Map<String, String> parseAiResponse(String response, List<Stock> stocks) {
        Map<String, String> recs = new HashMap<>();
        for (Stock s : stocks) {
            String t = s.getTicker();
            String text = extractTickerLine(response, t);
            recs.put(t, text);
        }
        return recs;
    }

    private String extractTickerLine(String text, String ticker) {
        if (text == null) return "AI Suggestion unavailable.";
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.toUpperCase().contains(ticker.toUpperCase())) return line.trim();
        }
        return "AI Suggestion: Hold or review before trading.";
    }
}
