package com.gymhomie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gymhomie.tools.Goal;


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
    private void updateSummary() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            //summary.setText("Active Goals:");
        }
    }

}
