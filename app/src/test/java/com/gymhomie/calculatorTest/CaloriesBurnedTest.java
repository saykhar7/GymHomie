package com.gymhomie.calculatorTest;

import com.gymhomie.calculator.CaloriesBurned;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

public class CaloriesBurnedTest {
    @Test
    public void calculate_metric_system_constructor_test() throws IOException {
        CaloriesBurned c = new CaloriesBurned("ran 3 miles", 85.3, 26, 177.8, "male");
        try {
            c.calculate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void calculate_english_system_constructor_double_test() throws IOException {
        double heightFeet = 5;
        double weightLbs = 188;
        int age = 26;
        double heightInches = 10;
        String gender = "male";
        String exercise = "ran 3 miles";
        double duration = 50;
        CaloriesBurned c = new CaloriesBurned(exercise, heightFeet, weightLbs, age,heightInches, gender, duration);
        try {
            c.calculate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void calculate_english_system_constructor_test() throws IOException {
        CaloriesBurned c = new CaloriesBurned("ran 3 miles", 5, 188, 26,10, "male");
        try {
            c.calculate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void calculate_english_system_constructor_Double_test() throws IOException {
        String heightFeet = "5";
        String weightLbs = "188";
        String age = "27";
        String heightInches = "10";
        String gender = "male";
        String exercise = "ran 3 miles";
        String duration = "50";
        CaloriesBurned c = new CaloriesBurned(exercise, Double.parseDouble(heightFeet), Double.parseDouble(weightLbs), Integer.parseInt(age),Double.parseDouble(heightInches), gender, Double.parseDouble(duration));
        try {
            c.calculate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
