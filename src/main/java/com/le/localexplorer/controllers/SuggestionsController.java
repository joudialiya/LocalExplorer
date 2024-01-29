package com.le.localexplorer.controllers;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.errors.ApiException;
import com.le.localexplorer.services.*;
import com.le.localexplorer.models.Activity;
import com.le.localexplorer.models.UserSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SuggestionsController{
    @Autowired
    IActivityProviderService activityGPTProvider;
    @Autowired
    IWeatherService weatherOpenMeteoService;
    @Autowired
    ITimeService timeAPIService;
    @Autowired
    ISessionService sessionService;

    @RequestMapping ("/activity")
    public ResponseEntity<Activity> next(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestBody String body)
            throws IOException, InterruptedException, ApiException {

        UserSession user;
        // retrieve the user session object
        if (sessionService.isNew())
            user = sessionService.create(longitude, latitude);
        else
            user = sessionService.get();

        // handling the user feedback, adjusting the preferences
        handlingFeedback(body, user);

        System.out.println(user);

        // retrieve the next activity
        Activity activity;
        activity = activityGPTProvider.next();
        System.out.println(activity.getDescription());
        return ResponseEntity.ok(activity);
    }
    private void handlingFeedback(String body, UserSession user)
    {
        if (body != null && !body.isEmpty())
        {
            //System.out.println("Handling Feedback + Request Body: " + body);
            //System.out.println("Preferences: " + user.getPreferences());
            JsonObject tree = (JsonObject) JsonParser.parseString(body);
            boolean liked = tree.get("liked").getAsBoolean();
            JsonArray types = tree.get("types").getAsJsonArray();
            if (liked)
            {
                if (user.getPreferences() == null)
                    user.setPreferences(new HashMap<>());
                for (int i=0; i < types.size(); ++i)
                {
                    String type = types.get(i).getAsString();
                    if (user.getPreferences().containsKey(type))
                        user.getPreferences().put(type, user.getPreferences().get(type) + 1);
                    else
                        user.getPreferences().put(type, 1);
                }
            }
            //System.out.println("New preferences: " + user.getPreferences());
        }
    }

}
