package com.le.localexplorer.services;

import com.google.maps.errors.ApiException;
import com.le.localexplorer.models.UserSession;

import java.io.IOException;

public interface ISessionService {
    boolean isNew();
    UserSession create(double longitude, double latitude) throws IOException, InterruptedException, ApiException;
    UserSession get();
}
