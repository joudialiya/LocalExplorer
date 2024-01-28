package com.le.localexplorer.services.placesapi;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class PlacesRequest {
    public List<String> includedPrimaryTypes;
    public Integer maxResultCount;
    public LocationRestriction locationRestriction;

    @Builder
    static public class Center {

        public Double latitude;
        public Double longitude;
    }

    @Builder
    static public class Circle {

        public Center center;
        public Double radius;

    }
    @Builder
    static public class LocationRestriction {
        public Circle circle;

    }

    public PlacesRequest(double longitude, double latitude, double radius, List<String> includedPrimaryTypes, int max)
    {
        this.includedPrimaryTypes = includedPrimaryTypes;
        this.maxResultCount = max;
        this.locationRestriction = LocationRestriction.builder()
                .circle(Circle.builder()
                        .center(Center.builder()
                                .longitude(longitude)
                                .latitude(latitude)
                                .build())
                        .radius(radius)
                        .build())
                .build();
    }

    public static final String[] sports = {
            "athletic_field", "fitness_center", "golf_course", "gym", "playground",
            "ski_resort", "sports_club", "sports_complex", "stadium", "swimming_pool"
    };
    public static final String[] shopping = {
            "auto_parts_store", "bicycle_store", "book_store", "cell_phone_store", "clothing_store",
            "convenience_store", "department_store", "discount_store", "electronics_store", "furniture_store",
            "gift_shop", "grocery_store", "hardware_store", "home_goods_store", "home_improvement_store",
            "jewelry_store", "liquor_store", "market", "pet_store", "shoe_store",
            "shopping_mall", "sporting_goods_store", "store", "supermarket", "wholesaler"
    };
    public static final String[] food = {
            "american_restaurant", "bakery", "bar", "barbecue_restaurant", "brazilian_restaurant",
            "breakfast_restaurant", "brunch_restaurant", "cafe", "chinese_restaurant", "coffee_shop",
            "fast_food_restaurant", "french_restaurant", "greek_restaurant", "hamburger_restaurant", "ice_cream_shop",
            "indian_restaurant", "indonesian_restaurant", "italian_restaurant", "japanese_restaurant", "korean_restaurant",
            "lebanese_restaurant", "meal_delivery", "meal_takeaway", "mediterranean_restaurant", "mexican_restaurant",
            "middle_eastern_restaurant", "pizza_restaurant", "ramen_restaurant", "restaurant", "sandwich_shop",
            "seafood_restaurant", "spanish_restaurant", "steak_house", "sushi_restaurant", "thai_restaurant",
            "turkish_restaurant", "vegan_restaurant", "vegetarian_restaurant", "vietnamese_restaurant"
    };
    public static final String[] entertainment = {
            "amusement_center", "amusement_park", "aquarium", "banquet_hall", "bowling_alley",
            "casino", "community_center", "convention_center", "cultural_center", "dog_park",
            "event_venue", "hiking_area", "historical_landmark", "marina", "movie_rental",
            "movie_theater", "national_park", "night_club", "park", "tourist_attraction",
            "visitor_center", "wedding_venue", "zoo"
    };
}
