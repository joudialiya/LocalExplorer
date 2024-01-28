package com.le.localexplorer.models;

import com.le.localexplorer.services.placesapi.PlacesResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Map;

@Data
@Builder
public class UserSession implements Serializable {
    double longitude;
    double latitude;
    String weatherCondition;
    LocalTime time;
    int totalLikes;
    Map<String, Integer> preferences;
}
