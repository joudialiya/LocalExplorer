package com.le.localexplorer.services.gpt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AllArgsConstructor
public class GPTClient {
    final String API_URL = "https://api.openai.com/v1/chat/completions";
    String apiKey;
    public GPTResponse send(GPTRequest req) throws IOException, InterruptedException {

        HttpResponse<String> response = null;
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(API_URL))
                .POST(HttpRequest.BodyPublishers.ofString(new GsonBuilder().create().toJson(req)))
                .setHeader("Authorization", "Bearer " + apiKey)
                .setHeader("Content-Type", "Application/json")
                .build();

        response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return new Gson().fromJson(response.body(), GPTResponse.class);
    }
}
