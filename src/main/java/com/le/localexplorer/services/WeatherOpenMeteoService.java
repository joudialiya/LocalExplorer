package com.le.localexplorer.services;

import com.google.gson.Gson;
import com.le.localexplorer.services.openmeteo.OMResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherOpenMeteoService implements IWeatherService{
    static final String API_URL = "https://api.open-meteo.com/v1/forecast/?current=temperature_2m,weather_code";

    public String current(double longitude, double latitude)
            throws IOException, InterruptedException {
        HttpResponse<String> response = null;
        String url = API_URL + "&longitude=" + longitude + "&latitude=" + latitude;
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .setHeader("Content-Type", "Application/json")
                .build();
        System.out.println("Weather API: " + url);
        response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), OMResponse.class).getWeatherCondition();
    }
}
