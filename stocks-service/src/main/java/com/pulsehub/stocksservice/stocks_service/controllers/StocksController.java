package com.pulsehub.stocksservice.stocks_service.controllers;

import com.pulsehub.stocksservice.stocks_service.model.Stock;
import com.pulsehub.stocksservice.stocks_service.service.StocksService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:5173")
public class StocksController {

    private final StocksService stocksService;
    private final WebClient webClient;

    public StocksController(StocksService stocksService) {
        this.stocksService = stocksService;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8087/api/ai/ask")
                .build();
    }

    @GetMapping("/list")
    public List<Stock> getStocks() {
        return stocksService.getStockList();
    }

    @GetMapping("/price")
    public Map<String, Object> getStockPrice(@RequestParam String ticker) {
        Stock stock = stocksService.getStockByTicker(ticker);

        String aiInsight;
        try {
            Map<String, String> request = Map.of(
                    "question", "Give a short, clear, 2-line analysis for stock: " + ticker,
                    "context", "Price: " + stock.getPrice() + ", Change: " + stock.getChange()
            );

            Map<?, ?> aiResponse = webClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            aiInsight = aiResponse != null && aiResponse.get("response") != null
                    ? aiResponse.get("response").toString()
                    : "AI insight unavailable.";
        } catch (Exception e) {
            aiInsight = "AI service not reachable.";
        }

        return Map.of(
                "ticker", stock.getTicker(),
                "price", stock.getPrice(),
                "change", stock.getChange(),
                "aiInsight", aiInsight
        );
    }
}
