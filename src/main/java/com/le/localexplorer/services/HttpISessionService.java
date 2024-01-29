package com.le.localexplorer.services;

import com.google.maps.errors.ApiException;
import com.le.localexplorer.models.UserSession;
import com.le.localexplorer.services.placesapi.PlacesRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class HttpISessionService implements ISessionService {

    @Autowired
    NewPlacesAPIService placesAPIService;
    @Autowired
    WeatherOpenMeteoService weatherOpenMeteoService;
    @Autowired
    TimeAPIService timeAPIService;
    @Autowired
    HttpSession session;

    @Override
    public boolean isNew()
    {
        return session.isNew();
    }
    @Override
    public UserSession create(double longitude, double latitude) throws IOException, InterruptedException, ApiException {
        UserSession user = null;

        if (session.isNew()) {
            System.out.println("new session");

            user = initUser(longitude, latitude);

            session.setAttribute("sessionObject", user);
        }
        else {
            System.out.println("session exists");
            user = (UserSession) (session.getAttribute("sessionObject"));
        }
        return user;
    }
    @Override
    public UserSession get() {
        UserSession user = (UserSession) (session.getAttribute("sessionObject"));
        return user;
    }

    private UserSession initUser(double longitude, double latitude)
            throws IOException, InterruptedException, ApiException {
        UserSession user = UserSession.builder()
                .longitude(longitude)
                .latitude(latitude)
                .weatherCondition(weatherOpenMeteoService.current(longitude, latitude))
                .time(timeAPIService.current(longitude, latitude))
                .preferences(new HashMap<>())
                .build();
        // initialize the preferences map
        List<String> types = new ArrayList<>();
        // I've chosen to initialize it with only entertainment and sports type of people,
        // we can add more types we want to
        types.addAll(Arrays.asList(PlacesRequest.entertainment));
        types.addAll(Arrays.asList(PlacesRequest.sports));
        for (var type: types)
            user.getPreferences().put(type, 0);

        return user;
    }
}
