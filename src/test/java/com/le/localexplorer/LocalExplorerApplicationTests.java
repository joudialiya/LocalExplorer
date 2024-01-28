package com.le.localexplorer;

import com.le.localexplorer.services.ActivityGPTProvider;
import com.le.localexplorer.services.IWeatherService;
import com.le.localexplorer.services.gpt.GPTClient;
import com.le.localexplorer.services.gpt.GPTMessage;
import com.le.localexplorer.services.gpt.GPTRequest;
import com.le.localexplorer.services.placesapi.PlacesClient;
import com.le.localexplorer.services.placesapi.PlacesRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class LocalExplorerApplicationTests {

    @Autowired
    IWeatherService weatherService;
    @Autowired
    ActivityGPTProvider activityGPTProvider;
    @Test
    void contextLoads() throws IOException, InterruptedException {
        final String OPENAI_KEY = "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";
        var req = new GPTRequest().addMessage(GPTMessage.USER, "give me hello world in python");

        GPTClient client = new GPTClient(OPENAI_KEY);
        System.out.println(client.send(req).getChoices().get(0).getMessage().getContent());
    }
    @Test
    void testWeatherService() throws IOException, InterruptedException {
        System.out.println(weatherService.current(13.419, 52));
    }
    @Test
    void testPlaces() throws IOException, InterruptedException {
        final String GOOGLE_KEY = "AIzaSyCigD00c3X-UdEirr7ZYZzfZkKGx4woJbI";
        List<String> includeTypes = new ArrayList<>();
        includeTypes.addAll(Arrays.stream(PlacesRequest.sports).toList());
        PlacesRequest request = new PlacesRequest(2.349014, 48.864716, 2000, includeTypes, 10);
        PlacesClient client = new PlacesClient();
        client.apiKey = GOOGLE_KEY;
        System.out.println(client.send(request));
    }
}
