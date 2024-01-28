package com.le.localexplorer.services;

import com.le.localexplorer.services.placesapi.PlacesClient;
import com.le.localexplorer.services.placesapi.PlacesRequest;
import com.le.localexplorer.services.placesapi.PlacesResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NewPlacesAPIService implements IPlacesService {
    public PlacesResponse next(double longitude, double latitude, List<String> types)
            throws IOException, InterruptedException {
        final String GOOGLE_KEY = "AIzaSyCigD00c3X-UdEirr7ZYZzfZkKGx4woJbI";

        PlacesRequest request = new PlacesRequest(longitude, latitude, 2000, types, 10);
        PlacesClient client = new PlacesClient();
        client.apiKey = GOOGLE_KEY;
        return  client.send(request);
    }
}
