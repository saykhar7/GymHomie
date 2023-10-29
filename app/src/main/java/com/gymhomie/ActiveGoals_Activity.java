package com.gymhomie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.gymhomie.tools.Goal;

import java.util.Map;

public class ActiveGoals_Activity extends AppCompatActivity {
    private Goal goal = new Goal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_goals);
        LinearLayout goalContainer = findViewById(R.id.goalContainer);
        Button button = findViewById(R.id.yourButtonId);
        goal = new Goal();
        // Retrieve the parameters sent from Goal_Activity
        boolean stepCounterEnabled = getIntent().getBooleanExtra("stepcounter", false);
        boolean waterIntakesEnabled = getIntent().getBooleanExtra("waterintakes", false);
        boolean profileEnabled = getIntent().getBooleanExtra("profile", false);
        boolean exerciseEnabled = getIntent().getBooleanExtra("exercise", false);
        boolean workoutsEnabled = getIntent().getBooleanExtra("workouts", false);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                processGoals(stepCounterEnabled, waterIntakesEnabled, profileEnabled, exerciseEnabled, workoutsEnabled);
            }
        });
    }

    private void processGoals(boolean stepCounterEnabled, boolean waterIntakesEnabled, boolean profileEnabled, boolean exerciseEnabled, boolean workoutsEnabled) {
        LinearLayout goalContainer = findViewById(R.id.goalContainer);

        if (stepCounterEnabled) {
            // Query the Firestore collection related to Step Counter
            String end_date = "";
            String start_date = "";
            for(Map<String, Object> map: goal.getStepGoals()) {
                TextView textView = new TextView(this);
                start_date = (String) map.get("start_month") + "/" + map.get("start_day") + "/" + map.get("start_year");
                end_date = (String) map.get("end_month") + "/" + map.get("end_day") + "/" + map.get("end_year");
                textView.setText("Goal Title: " + map.get("title") + ", Goal Type: " + map.get("type") + ", Target Steps: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                goalContainer.addView(textView);
            }
        }
        if (waterIntakesEnabled) {
            // Query the Firestore collection related to Water Intakes
            String end_date = "";
            String start_date = "";
            for(Map<String, Object> map: goal.getHydrationGoals()){
                TextView textView = new TextView(this);
                start_date = (String) map.get("start_month") + "/" + map.get("start_day") + "/" + map.get("start_year");
                end_date = (String) map.get("end_month") + "/" + map.get("end_day") + "/" + map.get("end_year");
                textView.setText("Goal Title: " + map.get("title") + ", Goal Type: " + map.get("type") + ", Target Ounces: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                goalContainer.addView(textView);
            }
        }
        if (profileEnabled) {
            // Query the Firestore collection related to Profile
            String end_date = "";
            String start_date = "";
            for(Map<String, Object> map: goal.getWeightGoals()){
                TextView textView = new TextView(this);
                start_date = (String) map.get("start_month") + "/" + map.get("start_day") + "/" + map.get("start_year");
                end_date = (String) map.get("end_month") + "/" + map.get("end_day") + "/" + map.get("end_year");
                textView.setText("Goal Title: " + map.get("title") + ", Target Weight: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                goalContainer.addView(textView);
            }
        }
        if (exerciseEnabled) {
            // Query the Firestore collection related to Exercise
            String end_date = "";
            String start_date = "";
            for(Map<String, Object> map: goal.getExerciseGoals()){
                TextView textView = new TextView(this);
                start_date = (String) map.get("start_month") + "/" + map.get("start_day") + "/" + map.get("start_year");
                end_date = (String) map.get("end_month") + "/" + map.get("end_day") + "/" + map.get("end_year");
                textView.setText("Goal Title: " + map.get("title") + ", Goal Type: " + map.get("type") + ", Target Weight: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                goalContainer.addView(textView);
            }
        }
        if (workoutsEnabled) {
            // Query the Firestore collection related to Workouts
            String end_date = "";
            String start_date = "";
            for(Map<String, Object> map: goal.getWorkoutGoals()){
                TextView textView = new TextView(this);
                start_date = (String) map.get("start_month") + "/" + map.get("start_day") + "/" + map.get("start_year");
                end_date = (String) map.get("end_month") + "/" + map.get("end_day") + "/" + map.get("end_year");
                if(map.get("type").toString().equalsIgnoreCase("weekly")) {
                    textView.setText("Goal Title: " + map.get("title") + ", Goal Type: " + map.get("type") + ", Target Days: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                }
                else if (map.get("type").toString().equalsIgnoreCase("daily")) {
                    textView.setText("Goal Title: " + map.get("title") + ", Goal Type: " + map.get("type") + ", Target Hours: " + map.get("target") + ", Start Date: " + start_date + ", End Date: " + end_date);
                }
                goalContainer.addView(textView);
            }
        }
    }
}
