package com.le.localexplorer.services.placesapi;


import lombok.Data;

import java.util.List;

@Data
public class PlacesResponse {
    public List<Place> places;
}
