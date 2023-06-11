package com.gymhomie.calculatorTest;

import com.gymhomie.calculator.CaloriesBurned;
import com.gymhomie.location.LocationHandler;

import org.junit.Test;

import java.io.IOException;

public class CaloriesBurnedTest {
    @Test
    public void calculate_metric_system_constructor_test() throws IOException {
        CaloriesBurned c = new CaloriesBurned("ran 3 miles", 85.3, 26, 177.8, "male");
            c.calculate();
    }
    @Test
    public void calculate_english_system_constructor_test() throws IOException {
        CaloriesBurned c = new CaloriesBurned("ran 3 miles", 5, 188, 26,10.5, "male");
        c.calculate();
    }
}
