package com.pulsehub.stocksservice.stocks_service.model;

public class Stock {
    private String ticker;
    private double price;
    private String currency;
    private String change;
    private String aiRecommendation;

    public Stock() {}

    public Stock(String ticker, double price, String currency, String change, String aiRecommendation) {
        this.ticker = ticker;
        this.price = price;
        this.currency = currency;
        this.change = change;
        this.aiRecommendation = aiRecommendation;
    }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getChange() { return change; }
    public void setChange(String change) { this.change = change; }
    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }
}
