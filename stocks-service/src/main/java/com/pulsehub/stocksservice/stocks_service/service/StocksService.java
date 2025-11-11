package com.pulsehub.stocksservice.stocks_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsehub.stocksservice.stocks_service.model.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StocksService {

    @Value("${finnhub.api.key:}")
    private String apiKey; // optional: leave blank for mock mode

    @Value("${ai.service-url:http://localhost:8087/api/ai/ask}")
    private String aiServiceUrl;

    private static final String BASE_URL = "https://finnhub.io/api/v1/quote?symbol=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // 🧠 Cache last AI response to avoid overloading
    private static final Map<String, String> aiCache = new ConcurrentHashMap<>();
    private static long lastAiCallTime = 0;

    public List<Stock> getStockList() {
        String[] tickers = {
                "AAPL", "MSFT", "GOOG", "AMZN", "META", "NFLX", "TSLA",
                "NVDA", "AMD", "INTC", "RELIANCE:NS", "TCS:NS", "INFY:NS",
                "HDFCBANK:NS", "ICICIBANK:NS"
        };

        List<Stock> list = new ArrayList<>();
        for (String ticker : tickers) {
            list.add(getStockByTicker(ticker));
        }

        // If all failed, return mock data
        if (list.isEmpty()) {
            list = getMockStockList();
        }

        // 🧠 Fetch single AI suggestion for the list
        String aiResponse = getAiInsight(list);

        // Apply AI response to all stocks
        for (Stock s : list) {
            s.setAiRecommendation(aiResponse);
        }

        return list;
    }

    // ---------------- AI Integration ----------------

    private String getAiInsight(List<Stock> stocks) {
        try {
            long now = System.currentTimeMillis();

            // 🕒 Use cached response if less than 60 seconds old
            if (now - lastAiCallTime < 60_000 && aiCache.containsKey("stocks_ai")) {
                return aiCache.get("stocks_ai");
            }

            StringBuilder prompt = new StringBuilder("Analyze these stocks and provide a one-line investment insight:\n");
            for (Stock s : stocks) {
                prompt.append(String.format("- %s: Price %.2f (%s)\n", s.getTicker(), s.getPrice(), s.getChange()));
            }

            Map<String, String> req = Map.of("question", prompt.toString());
            String response = restTemplate.postForObject(aiServiceUrl, req, String.class);

            if (response == null || response.isBlank()) {
                response = "AI Suggestion: Diversify across stable tickers 📊";
            }

            aiCache.put("stocks_ai", response);
            lastAiCallTime = now;

            return response;

        } catch (Exception e) {
            System.err.println("⚠️ AI fetch failed: " + e.getMessage());
            return "AI Suggestion: Hold strong companies like AAPL, MSFT, and NVDA for stability 💡";
        }
    }

    // ---------------- Stock Data Fetch ----------------

    public Stock getStockByTicker(String ticker) {
        // If no API key configured → skip API call
        if (apiKey == null || apiKey.isBlank()) {
            return getMockStock(ticker);
        }

        try {
            String url = BASE_URL + ticker + "&token=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode data = mapper.readTree(response);

            double price = data.path("c").asDouble();
            double changeVal = data.path("dp").asDouble();
            String change = String.format("%+.2f%%", changeVal);

            if (price <= 0) return getMockStock(ticker);

            return new Stock(ticker, price, "USD", change, "AI analyzing...");

        } catch (Exception e) {
            System.err.println("Error fetching stock " + ticker + ": " + e.getMessage());
            return getMockStock(ticker);
        }
    }

    // ---------------- MOCK DATA ----------------

    private List<Stock> getMockStockList() {
        List<Stock> mock = new ArrayList<>();
        mock.add(new Stock("AAPL", 190.50, "USD", "+1.20%", "Buy the dip — steady performer."));
        mock.add(new Stock("MSFT", 340.10, "USD", "-0.35%", "Strong fundamentals, hold."));
        mock.add(new Stock("GOOG", 142.78, "USD", "+0.45%", "Search and AI growth, buy."));
        mock.add(new Stock("TSLA", 248.22, "USD", "-2.10%", "Volatile, accumulate slowly."));
        mock.add(new Stock("AMZN", 131.00, "USD", "+0.85%", "Retail rebound expected."));
        return mock;
    }

    private Stock getMockStock(String ticker) {
        double base = 100 + Math.random() * 1000;
        double change = (Math.random() - 0.5) * 5;
        String changeStr = String.format("%+.2f%%", change);
        String rec = change >= 0 ? "Buy" : "Sell";
        return new Stock(ticker, base, "USD", changeStr, rec + " — mock data");
    }
}
