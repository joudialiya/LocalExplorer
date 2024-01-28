package com.le.localexplorer.services;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;


// I am not using it anymore
@Service
public class OldPlacesAPIProvider {
    final String GOOGLE_KEY = "AIzaSyCigD00c3X-UdEirr7ZYZzfZkKGx4woJbI";
    Random random = new Random();
    public PlacesSearchResponse nextByType(double longitude, double latitude, String type,  String token)
            throws IOException, InterruptedException, ApiException {

        var builder = new GeoApiContext.Builder();
        builder.apiKey(GOOGLE_KEY);

        var req = new NearbySearchRequest(builder.build())
                .location(new LatLng(latitude, longitude))
                .radius(2000)
                .openNow(true)
                .type(PlaceType.valueOf(type));
        if (token != null)
            req.pageToken(token);
        System.out.println("Places API retrieve places for type " + type);
        PlacesSearchResponse resp = req.await();
        return resp;
    }

}
