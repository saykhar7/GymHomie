package com.gymhomie.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CaloriesBurned {
    private static String apiUrl = "https://trackapi.nutritionix.com/v2/natural/exercise";
    private String appId = "5a866100";
    private String appKey = "913c786b68207e89ef1013d9a7dbcf3b";
    // Set the request parameters
    private String exerciseQuery;
    private String gender;
    private double weight;
    private double height;
    private int age;
    // Constructor that uses metric system
    public CaloriesBurned(String exerciseQuery, double weight, int age, double height, String gender) {
        this.exerciseQuery = exerciseQuery;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }
    // Constructor that converts height and weight from English to metric system
    public CaloriesBurned(String exerciseQuery, double heightFeet, double weightLbs, int age, double heightInches, String gender) {
        this.exerciseQuery = exerciseQuery;
        this.gender = gender;
        this.weight = poundsToKilograms(weightLbs);
        this.height = feetAndInchesToCentimeters(heightFeet, heightInches);
        this.age = age;
    }

    public String getExerciseQuery() {
        return exerciseQuery;
    }

    public void setExerciseQuery(String exerciseQuery) {
        this.exerciseQuery = exerciseQuery;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public static double poundsToKilograms(double weightInPounds) {
        return weightInPounds * 0.45359237;
    }
    public static double feetAndInchesToCentimeters(double heightFeet, double heightInches) {
        double totalHeightInches = (heightFeet * 12) + heightInches;
        return totalHeightInches * 2.54;
    }

    public double calculate() {

        try {
            // Build the request body
            String requestBody = String.format("{\"query\":\"%s\",\"gender\":\"%s\",\"weight_kg\":%.2f,\"height_cm\":%.2f,\"age\":%d}",
                    exerciseQuery, gender, weight, height, age);

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("x-app-id", appId);
            connection.setRequestProperty("x-app-key", appKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println(response.toString());
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
