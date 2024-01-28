package com.le.localexplorer.services;

import com.google.maps.errors.ApiException;
import com.le.localexplorer.services.gpt.GPTClient;
import com.le.localexplorer.services.gpt.GPTMessage;
import com.le.localexplorer.services.gpt.GPTRequest;
import com.le.localexplorer.services.gpt.GPTResponse;
import com.le.localexplorer.services.openmeteo.OMResponse;
import com.le.localexplorer.models.Activity;
import com.le.localexplorer.models.UserSession;
import com.le.localexplorer.services.placesapi.Place;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ActivityGPTProvider implements IActivityProviderService {
    @Autowired
    NewPlacesAPIService placesAPIService;
    @Autowired
    HttpSession session;
    @Autowired
    ISessionService sessionService;
    final String OPENAI_KEY = "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";
    final Random random = new Random();
    public Activity next() throws IOException, InterruptedException, ApiException {
        UserSession user = sessionService.get();
        double factor = calculateGoOutsideFactor(user.getTime().getHour(), user.getWeatherCondition());
        System.out.println("GoOutsideFactor: " + factor);
        if (random.nextDouble(0, 1) <= factor)
            // outdoor activity
            return generateOutdoorActivity();
        else
            // indoor activity
            return generateIndoorActivity();
    }

    public double calculateGoOutsideFactor(int time, String weatherCondition) {
        double goOutsideFactor = 0.5; // Initialize factor

        // Time based adjustments:
        if (time >= 8 && time <= 20) { // Daytime
            goOutsideFactor += .2;
        } else if (time >= 4 && time <= 7) { // Early morning
            goOutsideFactor += .1;
        } else if (time >= 21 && time <= 23) { // Evening
            goOutsideFactor += 0.5;
        } else { // Nighttime (0-3)
            goOutsideFactor -= .1;
        }
        // Weather based adjustments:
        if (weatherCondition.equals(OMResponse.WeatherConditions.CLEAR) ||
                weatherCondition.equals(OMResponse.WeatherConditions.MAINLY_CLEAR) ||
                weatherCondition.equals(OMResponse.WeatherConditions.PARTLY_CLEAR)) {
            goOutsideFactor += .15;
        } else if (weatherCondition.equals(OMResponse.WeatherConditions.OVERCAST)) {
            goOutsideFactor += .05;
        } else if (weatherCondition.equals(OMResponse.WeatherConditions.FOG) ||
                weatherCondition.equals(OMResponse.WeatherConditions.LIGHT_DRIZZLE)) {
            goOutsideFactor -= .05;
        } else if (weatherCondition.equals(OMResponse.WeatherConditions.DENSE_DRIZZLE) ||
                weatherCondition.equals(OMResponse.WeatherConditions.RAINY) ||
                weatherCondition.equals(OMResponse.WeatherConditions.SNOWY)) {
            goOutsideFactor -= .15;
        }
        // Ensure factor stays within a reasonable range:
        goOutsideFactor = Math.max(goOutsideFactor, 0);
        goOutsideFactor = Math.min(goOutsideFactor, 1.);

        return goOutsideFactor;
    }
    public Activity generateIndoorActivity() throws IOException, InterruptedException {

        UserSession user = sessionService.get();

        Activity activity = new Activity();
        String prompt = "Give me directly an in indoor fun activity that i can do\n";

        prompt = prompt + "i will let u know that the whether outside is kinda " +
                user.getWeatherCondition().replace('_', ' ').toLowerCase() +
                " and the current time is " + user.getTime().toString() + "\n" +
                "Compile the information and provide me with a simple brief description of 70 word max of the activity without mentioning " +
                "that you have analysed the weather or the time directly.\n";
        System.out.println("indoor");

        GPTRequest request = new GPTRequest().addMessage(GPTMessage.USER, prompt);
        GPTResponse response = new GPTClient(OPENAI_KEY).send(request);

        if (response == null)
            return null;

        String description  = response.getChoices().get(0).getMessage().getContent();
        activity.setDescription(description);
        activity.setType("indoor");
        return activity;
    }
    // an out dor activity is generated based on the place
    public Activity generateOutdoorActivity() throws IOException, InterruptedException, ApiException {

        UserSession user = (UserSession) session.getAttribute("sessionObject");

        Activity activity = new Activity();
        // we select a place
        Place place = getRecomendedPlace();

        String prompt = "Place name: " + place.displayName.text + "\n" +
                "Place types: " + place.types + "\n" +
                "Give me directly one fun little outdoor activity based on the given place information.\n" +
                "While giving me the suggestion please take into account the name semantic meaning as well as place types.\n" +
                "Compile the information and provide me with a simple brief description of 70 word max of the activity without mentioning " +
                "that you have analysed the name semantically or referred to the google map categories.\n";

        GPTRequest request = new GPTRequest().addMessage(GPTMessage.USER, prompt);
        GPTResponse response = new GPTClient(OPENAI_KEY).send(request);
        System.out.println("GPT response: " + response);
        if (response == null)
            return null;

        String description  = response.getChoices().get(0).getMessage().getContent();
        activity.setPlace(place);
        activity.setDescription(description);
        activity.setType("outdoor");
        return activity;
    }

    // this function recommend a place it can be ran dom place from the nearby, or based on the user preferences
    public Place getRecomendedPlace() throws IOException, InterruptedException {
        // we set an exploration threshold, so that the system som times select a place randomly
        // not based on the user preferences, to introduce exploration
        final double exploration_factor = .25;
        if (random.nextDouble(0, 1) < exploration_factor)
        {
            return getRandomPlace();
        }
        else {
            return getScoreBasedPlace();
        }
    }
    public Place getScoreBasedPlace() throws IOException, InterruptedException {
        UserSession user = sessionService.get();

        // calculate the scores for each type based on the user preferences
        Map<String, Double> scores = new HashMap<>();
        for(var type: user.getPreferences().keySet()) {
            if (user.getTotalLikes() == 0)
                scores.put(type, 0.);
            else
                scores.put(type, user.getPreferences().get(type).doubleValue() / user.getTotalLikes());
        }
        System.out.println(scores);

        // select randomly based o the score of each type
        List<Place> places = null;

        // iterate through the types till we find a type in witch there are close places.
        // the reasoning behind that is that somme times we may not find places in the aria
        // that match the user preferences s we go to the next best chose till we fide a place
        while (places == null || places.isEmpty()) {
            // select the max score with a little decay
            // the decay is to introduce more perturbation to the system
            double decay = random.nextDouble(0, 0.5);
            double max = Collections.max(scores.values()) * (1 - decay);
            // make sure that the score is always >= 0
            max = Math.max(max, 0);

            String selectedType = null;
            List<String> types = new ArrayList<>(scores.keySet());
            // shuffle the types before the search to introduce more perturbation to the system
            Collections.shuffle(types);
            // searching for the type
            for (var type : types ){
                if (scores.get(type) >= max) {
                    selectedType = type;
                    break;
                }
            }
            System.out.println("Place type: " + selectedType);
            // searching for places based on the selected type
            places = placesAPIService.next(
                    user.getLongitude(),
                    user.getLatitude(),
                    Collections.singletonList(selectedType)).places;
            // we remove the types from the scores map,
            // in the case we did not find places in that type so the next iteration will not include it
            scores.remove(selectedType);
        }

        System.out.println("Places list: " + places);
        // we find the places, we select one place randomly
        return places.get(random.nextInt(0, places.size()));
    }
    public Place getRandomPlace() throws IOException, InterruptedException {
        UserSession user = sessionService.get();
        // get the types
        List<String> types = new ArrayList<>(user.getPreferences().keySet());

        List<Place> places = null;

        // iterate randomly through the types till we find a type in witch there are places
        while (places == null || places.isEmpty()) {

            Collections.shuffle(types);
            String selectedType = types.get(random.nextInt(0, types.size()));

            System.out.println(selectedType);
            places = placesAPIService.next(
                    user.getLongitude(),
                    user.getLatitude(),
                    Collections.singletonList(selectedType)).places;
            types.remove(selectedType);
        }
        // select a ran dom place from that type
        return places.get(random.nextInt(0, places.size()));
    }
}
