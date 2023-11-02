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
    private int currentImageIndex = 0;
    private final int[] imageResources = {
            R.drawable.jogging,
            R.drawable.water_bottle,
            R.drawable.body_scan,
            R.drawable.barbell,
            R.drawable.health
    };
    private final String[] titleResources = {
            "Step Goals",
            "Hydration Goals",
            "Weight Goals",
            "Exercise Goals",
            "Workout Goals"
    };
    Goal goal = new Goal();
    private final boolean[] hasGoals = new boolean[imageResources.length];
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
                    if(goal.getGoalCollection().get(0)){
                        // start active goals intent
                        intent.putExtra("title", "steps");
                        String start_date = "";
                        String end_date = "";
                        String target_steps = "";
                        String current_steps = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getStepGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_steps = map.get("target").toString();
                            current_steps = String.valueOf(goal.getCurrent_steps());
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_steps);
                            counter++;
                            intent.putExtra(counter.toString(), current_steps);
                            counter++;
                        }
                    } else if (!goal.getGoalCollection().get(0)) {
                        // TODO: Setup no goals screen

                    }
                }else if(currentImageIndex == 1){
                    if(goal.getGoalCollection().get(1)){
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
                    } else if (!goal.getGoalCollection().get(1)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 2){
                    if(goal.getGoalCollection().get(2)){
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
                    } else if (!goal.getGoalCollection().get(2)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 3){
                    if(goal.getGoalCollection().get(3)){
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
                    } else if (!goal.getGoalCollection().get(3)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 4){
                    if(goal.getGoalCollection().get(4)){
                        // start active goals intent
                        intent.putExtra("title", "workout");
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
                    } else if (!goal.getGoalCollection().get(4)) {
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
                if(currentImageIndex == 0) {
                    if(goal.getGoalCollection().get(0)){
                        // start active goals intent
                        intent.putExtra("title", "steps");
                        if (goal.getCompletedStepGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target_steps = "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getCompletedStepGoals()) {
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
                        }
                        if(goal.getNonActiveStepGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target_steps = "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getCompletedStepGoals()) {
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
                        }
                    } else if (!goal.getGoalCollection().get(0)) {
                        // TODO: Setup no goals screen

                    }
                }else if(currentImageIndex == 1){
                    // start active goals intent
                    intent.putExtra("title", "hydration");
                    if (goal.getCompletedHydrationGoals().size() > 0){
                        String start_date = "";
                        String end_date = "";
                        String target_ounces = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getCompletedHydrationGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_ounces = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_ounces);
                            counter++;
                        }
                    }
                    if(goal.getCompletedHydrationGoals().size() > 0){
                        String start_date = "";
                        String end_date = "";
                        String target_ounces = "";
                        String type = "";
                        Integer counter = 0;
                        for(Map<String, Object> map: goal.getCompletedHydrationGoals()) {
                            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                            type =  map.get("type").toString();
                            target_ounces = map.get("target").toString();
                            intent.putExtra(counter.toString(), start_date);
                            counter++;
                            intent.putExtra(counter.toString(), end_date);
                            counter++;
                            intent.putExtra(counter.toString(), type);
                            counter++;
                            intent.putExtra(counter.toString(), target_ounces);
                            counter++;
                        }
                    }else if (!goal.getGoalCollection().get(1)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 2){
                    if(goal.getGoalCollection().get(2)){
                        // start active goals intent
                        intent.putExtra("title", "weight");
                        if (goal.getCompletedWeightGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target_weight = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getCompletedWeightGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target_weight = map.get("target").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target_weight);
                                counter++;
                            }
                        }
                        if(goal.getNonActiveWeightGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target_weight = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getNonActiveWeightGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target_weight = map.get("target").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target_weight);
                                counter++;
                            }
                        }
                    } else if (!goal.getGoalCollection().get(2)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 3){
                    if(goal.getGoalCollection().get(3)){
                        // start active goals intent
                        intent.putExtra("title", "exercise");
                        if (goal.getCompletedExerciseGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target = "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getCompletedExerciseGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target = map.get("target").toString();
                                type = map.get("type").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target);
                                counter++;
                                intent.putExtra(counter.toString(), type);
                                counter++;
                            }
                        }
                        if(goal.getNonActiveExerciseGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target = "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getNonActiveExerciseGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target = map.get("target").toString();
                                type = map.get("type").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target);
                                counter++;
                                intent.putExtra(counter.toString(), type);
                                counter++;
                            }
                        }
                    } else if (!goal.getGoalCollection().get(3)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 4){
                    if(goal.getGoalCollection().get(4)){
                        // start active goals intent
                        intent.putExtra("title", "workout");
                        if (goal.getCompletedWorkoutGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target= "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getCompletedWorkoutGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target = map.get("target").toString();
                                type = map.get("type").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target);
                                counter++;
                                intent.putExtra(counter.toString(), type);
                                counter++;
                            }
                        }
                        if(goal.getNonActiveWorkoutGoals().size() > 0){
                            String start_date = "";
                            String end_date = "";
                            String target = "";
                            String type = "";
                            Integer counter = 0;
                            for(Map<String, Object> map: goal.getNonActiveWorkoutGoals()) {
                                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                                target = map.get("target").toString();
                                type = map.get("type").toString();
                                intent.putExtra(counter.toString(), start_date);
                                counter++;
                                intent.putExtra(counter.toString(), end_date);
                                counter++;
                                intent.putExtra(counter.toString(), target);
                                counter++;
                                intent.putExtra(counter.toString(), type);
                                counter++;
                            }
                        }
                    } else if (!goal.getGoalCollection().get(4)) {
                        // TODO: Setup no goals screen
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
