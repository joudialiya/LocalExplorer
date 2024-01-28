package com.le.localexplorer.models;

import com.le.localexplorer.services.placesapi.Place;
import lombok.Data;

@Data
public class Activity {
    String description;
    String type;
    Place place;
}
