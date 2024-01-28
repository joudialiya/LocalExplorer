package com.le.localexplorer.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeAPIService implements ITimeService{
    final String API_URL = "https://timeapi.io/api/Time/current/coordinate/";
    public LocalTime current(double longitude, double latitude) throws IOException, InterruptedException {
        final String uri = API_URL +
                '?' +
                "latitude=" + latitude +
                '&' +
                "longitude=" + longitude;
        HttpResponse<String> response = null;

        HttpRequest req = HttpRequest
                .newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        System.out.println("Time API:" + uri);
        response = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        JsonObject tree = (JsonObject) JsonParser.parseString(response.body());
        LocalTime time = LocalTime.parse(
                tree.get("time").getAsString(),
                DateTimeFormatter.ofPattern("H:mm")
        );
        return time;
    }
}
