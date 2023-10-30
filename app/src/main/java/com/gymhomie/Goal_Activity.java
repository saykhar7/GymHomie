package com.gymhomie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gymhomie.tools.Goal;

import java.util.Map;


public class Goal_Activity extends AppCompatActivity {

    private ImageView artImageView;
    private TextView title;
    private TextView summary;
    private int currentImageIndex = 0;
    private int[] imageResources = {
            R.drawable.jogging,
            R.drawable.water_bottle,
            R.drawable.body_scan,
            R.drawable.weights,
            R.drawable.stopwatch_icon

    };
    private String[] titleResources = {
            "Step Goals",
            "Hydration Goals",
            "Weight Goals",
            "Exercise Goals",
            "Workout Goals"
    };
    Goal goal = new Goal();
    private boolean[] hasGoals = new boolean[imageResources.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        artImageView = findViewById(R.id.artImageView);
        title = findViewById(R.id.titleAboveImageView);
        //summary = findViewById(R.id.summaryTextView);

        updateArtImageView();
        updateTitle();
        //updateSummary();
        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);
        Button activeGoalButton = findViewById(R.id.activeGoalsButton);
        Button goalHistoryButton = findViewById(R.id.goalHistoryButton);

        activeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Goal_Activity.this, ActiveGoals_Activity.class);
                if(currentImageIndex == 0) {
                    if(goal.getGoalCollection().get(0) == true){
                        // start active goals intent
                        intent.putExtra("title", "steps");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getStepGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_steps = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                        }
                    } else if (goal.getGoalCollection().get(0) == false) {
                        // TODO: Setup no goals screen

                    }
                }else if(currentImageIndex == 1){
                    if(goal.getGoalCollection().get(1) == true){
                        // start active goals intent
                        intent.putExtra("title", "hydration");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getHydrationGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_steps = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                        }
                    } else if (goal.getGoalCollection().get(1) == false) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 2){
                    if(goal.getGoalCollection().get(2) == true){
                        // start active goals intent
                        intent.putExtra("title", "weight");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getWeightGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            target_steps = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                        }
                    } else if (goal.getGoalCollection().get(2) == false) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 3){
                    if(goal.getGoalCollection().get(3) == true){
                        // start active goals intent
                        intent.putExtra("title", "exercise");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getExerciseGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            target_steps = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                        }
                    } else if (goal.getGoalCollection().get(3) == false) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 4){
                    if(goal.getGoalCollection().get(4) == true){
                        // start active goals intent
                        intent.putExtra("title", "workouts");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getWorkoutGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_steps = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                        }
                    } else if (goal.getGoalCollection().get(4) == false) {
                        // TODO: Setup no goals screen
                    }
                }
                startActivity(intent);
            }
        });

        goalHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Goal_Activity.this, GoalHistory_Activity.class);
                for (int i = 0; i < goal.getGoalCollection().size(); i++){
                    if(i == 0 && goal.getGoalCollection().get(i) == true){
                        // Step Counter collection exists
                        intent.putExtra("stepcounter", true);
                    }
                    else if(i == 0 && goal.getGoalCollection().get(i) == false){
                        // Step Counter collection does not exist
                        intent.putExtra("stepcounter", false);
                    }
                    if(i == 1 && goal.getGoalCollection().get(i) == true){
                        // Water Intakes collection exists
                        intent.putExtra("waterintakes", true);
                    }
                    else if(i == 1 && goal.getGoalCollection().get(i) == false){
                        // Water Intakes collection exists
                        intent.putExtra("waterintakes", false);
                    }
                    if(i == 2 && goal.getGoalCollection().get(i) == true){
                        // Profile collection exists
                        intent.putExtra("profile", true);
                    }
                    else if(i == 2 && goal.getGoalCollection().get(i) == false){
                        // Profile collection exists
                        intent.putExtra("profile", false);
                    }
                    if(i == 3 && goal.getGoalCollection().get(i) == true){
                        // Exercise collection exists
                        intent.putExtra("exercise", true);
                    }
                    else if(i == 3 && goal.getGoalCollection().get(i) == false){
                        // Exercise collection exists
                        intent.putExtra("exercise", false);
                    }
                    if(i == 4 && goal.getGoalCollection().get(i) == true){
                        // Workouts collection exists
                        intent.putExtra("workouts", true);
                    }
                    else if(i == 4 && goal.getGoalCollection().get(i) == false){
                        // Workouts collection exists
                        intent.putExtra("workouts", false);
                    }
                }
                startActivity(intent);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex > 0) {
                    // index 0 is steps: target steps, goal type
                    // index 1 is hydration: target ounces, goal type
                    // index 2 is weight: target weight
                    // index 3 is exercise: target weight
                    // index 4 is workouts: goal type, daily/weekly
                    currentImageIndex--;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < imageResources.length - 1) {
                    // index 0 is steps: target steps, goal type
                    // index 1 is hydration: target ounces, goal type
                    // index 2 is weight: target weight
                    // index 3 is exercise: target weight
                    // index 4 is workouts: goal type, daily/weekly
                    currentImageIndex++;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });
    }

    private void updateArtImageView() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            artImageView.setImageResource(imageResources[currentImageIndex]);
        }
    }
    private void updateTitle() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            title.setText(titleResources[currentImageIndex]);
        }
    }

}
