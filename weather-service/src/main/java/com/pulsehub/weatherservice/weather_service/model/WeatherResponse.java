package com.pulsehub.weatherservice.weather_service.model;

public class WeatherResponse {
    private String city;
    private double temperature;
    private String condition;
    private String message;

    public WeatherResponse() {}

    public WeatherResponse(String city, double temperature, String condition, String message) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.message = message;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public String getMessage() {
        return message;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
