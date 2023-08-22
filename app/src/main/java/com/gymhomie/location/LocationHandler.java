package com.gymhomie.location;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LocationHandler {
    public void getLocation() throws IOException {
        String ipAddress = getIpAddress();
        String location = getLocationByIp(ipAddress);

        System.out.println("IP Address: " + ipAddress);
        System.out.println("Location: " + location);
    }

    private static String getIpAddress() throws IOException {
        URL url = new URL("https://api.ipify.org");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String ipAddress = reader.readLine();
        reader.close();
        connection.disconnect();

        return ipAddress;
    }

    private static String getLocationByIp(String ipAddress) throws IOException {
        URL url = new URL("http://ip-api.com/json/" + ipAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());

        if (jsonElement.isJsonObject()) {
            return String.valueOf(jsonElement.getAsJsonObject());
        }

        return null;
    }
}


