package com.pulsehub.weatherservice.weather_service.controllers;

import com.pulsehub.weatherservice.weather_service.model.WeatherResponse;
import com.pulsehub.weatherservice.weather_service.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Handles both ?city= and ?lat=&lon=
    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ) {
        WeatherResponse response;

        if (lat != null && lon != null) {
            response = weatherService.getWeatherByCoordinates(lat, lon);
        } else if (city != null && !city.isBlank()) {
            response = weatherService.getWeatherByCity(city);
        } else {
            response = new WeatherResponse(
                    "Unknown",
                    0.0,
                    "Unavailable",
                    "No location or city provided."
            );
        }

        return ResponseEntity.ok(response);
    }

}
