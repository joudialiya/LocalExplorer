package com.le.localexplorer.services.placesapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PlacesClient {
    final String API_URL = "https://places.googleapis.com/v1/places:searchNearby";
    public String apiKey;
    public PlacesResponse send(PlacesRequest req) throws IOException, InterruptedException {

        HttpResponse<String> response = null;
        String body = new GsonBuilder().create().toJson(req);

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(API_URL))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("X-Goog-Api-Key", apiKey)
                .setHeader("X-Goog-FieldMask", "places.displayName,places.id,places.primaryType,places.types")
                .setHeader("Content-Type", "Application/json")
                .build();

        response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), PlacesResponse.class);
    }
}
