package com.le.localexplorer.services;

import java.io.IOException;
import java.time.LocalTime;

public interface ITimeService {
    public LocalTime current(double longitude, double latitude)
            throws IOException, InterruptedException;
}
