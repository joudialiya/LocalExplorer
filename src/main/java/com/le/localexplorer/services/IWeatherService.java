package com.le.localexplorer.services;

import java.io.IOException;

public interface IWeatherService {
    public String current(double longitude, double latitude)
            throws IOException, InterruptedException;
}
