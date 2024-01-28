package com.le.localexplorer.services.openmeteo;

import lombok.ToString;

@ToString
public class OMResponse {
    public static class WeatherConditions
    {
        public static final String CLEAR = "CLEAR";
        public static final String MAINLY_CLEAR = "MAINLY_CLEAR";
        public static final String PARTLY_CLEAR = "PARTLY_CLEAR";
        public static final String OVERCAST = "OVERCAST";
        public static final String FOG = "FOG";
        public static final String LIGHT_DRIZZLE = "LIGHT_DRIZZLE";
        public static final String DENSE_DRIZZLE = "DENSE_DRIZZLE";
        public static final String RAINY = "RAINY";
        public static final String SNOWY = "SNOWY";
        public static final String UNKNOWN = "UNKNOWN";
    }
    public Double latitude;
    public Double longitude;
    public Double generation_time_ms;
    public Integer utc_offset_seconds;
    public String timezone;
    public String timezone_abbreviation;
    public Integer elevation;
    public CurrentUnits current_units;
    public Current current;
    public String getWeatherCondition()
    {
        if (this.current.weather_code == 0)
            return WeatherConditions.CLEAR;
        else if (this.current.weather_code == 1)
            return  WeatherConditions.MAINLY_CLEAR;
        else if (this.current.weather_code == 2)
            return  WeatherConditions.PARTLY_CLEAR;
        else if (this.current.weather_code == 3)
            return WeatherConditions.OVERCAST;
        else if ( this.current.weather_code >= 45 &&this.current.weather_code <= 48)
            return WeatherConditions.FOG;
        else if ( this.current.weather_code >= 51 && this.current.weather_code <= 53)
            return  WeatherConditions.LIGHT_DRIZZLE;
        else if ( this.current.weather_code >= 55 && this.current.weather_code <= 57)
            return  WeatherConditions.DENSE_DRIZZLE;
        else if ( this.current.weather_code >= 61 && this.current.weather_code <= 67)
            return  WeatherConditions.RAINY;
        else if (this.current.weather_code >= 71 && this.current.weather_code <= 82)
            return  WeatherConditions.SNOWY;
        return WeatherConditions.UNKNOWN;
    }
    static public class CurrentUnits {
        public String time;
        public String interval;
        public String temperature_2m;
        public String weather_code;

    }
    @ToString
    static public class Current {
        public String time;
        public Integer interval;
        public Double temperature_2m;
        public Integer weather_code;
    }
}
