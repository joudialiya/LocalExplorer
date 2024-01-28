package com.le.localexplorer.services;

import com.google.maps.errors.ApiException;
import com.le.localexplorer.models.Activity;

import java.io.IOException;

public interface IActivityProviderService {
    Activity next() throws IOException, InterruptedException, ApiException;
}
