package com.pulsehub.weatherservice.weather_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsehub.weatherservice.weather_service.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherResponse getWeatherByCity(String city) {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                return new WeatherResponse(city, 26.5, "Clear", "Mock data: set openweathermap.api.key to get real weather.");
            }

            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + apiKey + "&units=metric";

            String json = restTemplate.getForObject(url, String.class);
            Map<String, Object> map = mapper.readValue(json, Map.class);

            String cityName = (String) map.getOrDefault("name", "Unknown");

            Map<String, Object> main = (Map<String, Object>) map.get("main");
            double temp = main != null ? ((Number) main.getOrDefault("temp", 0.0)).doubleValue() : 0.0;

            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) map.get("weather");
            String condition = (weatherList != null && !weatherList.isEmpty())
                    ? (String) weatherList.get(0).getOrDefault("main", "Unavailable")
                    : "Unavailable";

            return new WeatherResponse(cityName, temp, condition, "Real-time data fetched successfully.");
        } catch (Exception e) {
            return new WeatherResponse(city, 0.0, "Unavailable", "Unable to fetch weather data.");
        }
    }

    public WeatherResponse getWeatherByCoordinates(Double lat, Double lon) {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                return new WeatherResponse("Unknown", 26.5, "Clear", "Mock data: set openweathermap.api.key to get real weather.");
            }

            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                    "&lon=" + lon +
                    "&appid=" + apiKey + "&units=metric";

            String json = restTemplate.getForObject(url, String.class);
            Map<String, Object> map = mapper.readValue(json, Map.class);

            String cityName = (String) map.getOrDefault("name", "Unknown");

            Map<String, Object> main = (Map<String, Object>) map.get("main");
            double temp = main != null ? ((Number) main.getOrDefault("temp", 0.0)).doubleValue() : 0.0;

            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) map.get("weather");
            String condition = (weatherList != null && !weatherList.isEmpty())
                    ? (String) weatherList.get(0).getOrDefault("main", "Unavailable")
                    : "Unavailable";

            return new WeatherResponse(cityName, temp, condition, "Weather data fetched successfully.");
        } catch (Exception e) {
            return new WeatherResponse("Unknown", 0.0, "Unavailable", "Unable to fetch weather for coordinates.");
        }
    }
}
