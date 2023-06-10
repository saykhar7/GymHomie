package com.gymhomie.locationTest;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.maps.errors.ApiException;
import com.gymhomie.location.LocationHandler;

import java.io.IOException;

public class LocationHandlerTest {
    @Test
    public void getLocation_test() {
        LocationHandler lh = new LocationHandler();
        try {
            lh.getLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

