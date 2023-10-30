package com.gymhomie;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gymhomie.tools.Goal;

public class ActiveGoals_Activity extends AppCompatActivity {
    private Goal goal = new Goal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_goals);

        LinearLayout goalContainer = findViewById(R.id.goalContainer);

        // Retrieve the parameters sent from Goal_Activity
        String title = getIntent().getStringExtra("title");
        Bundle extras = getIntent().getExtras();
        String text = "";
        String start_date = "";
        String end_date = "";
        String target = "";
        String type = "";
        if(title.equalsIgnoreCase("steps")){
            TextView textView;
            int inner_count = 0;
            for (String key : extras.keySet()) {
                if (key.equalsIgnoreCase("title")) {
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if (count == 0) {
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    } else if (count % 4 == 0) {
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    } else {
                        // TODO: goal entry
                        if (inner_count == 1) {
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        } else if (inner_count == 2) {
                            type = getIntent().getStringExtra(String.valueOf(count));
                        } else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            text = "Goal Type: " + type + ", Target Steps: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            textView = new TextView(this);
                            View itemLayout = getLayoutInflater().inflate(R.layout.activity_active_goals, null);
                            textView = itemLayout.findViewById(R.id.textView);
                            textView.setText(text);
                            goalContainer.addView(itemLayout);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("hydration")) {
            TextView textView;
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 4 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            text = "Goal Type: " + type + ", Target Ounces: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            View itemLayout = getLayoutInflater().inflate(R.layout.activity_active_goals, null);
                            textView = itemLayout.findViewById(R.id.textView);
                            textView.setText(text);
                            goalContainer.addView(itemLayout);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("weight")) {
            TextView textView;
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 3 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            text = "Target Weight: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            textView = new TextView(this);
                            View itemLayout = getLayoutInflater().inflate(R.layout.activity_active_goals, null);
                            textView = itemLayout.findViewById(R.id.textView);
                            textView.setText(text);
                            goalContainer.addView(itemLayout);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("exercise")) {
            TextView textView;
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 4 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            text = "Exercise Type: " + type + ", Target: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            textView = new TextView(this);
                            View itemLayout = getLayoutInflater().inflate(R.layout.activity_active_goals, null);
                            textView = itemLayout.findViewById(R.id.textView);
                            textView.setText(text);
                            goalContainer.addView(itemLayout);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("workouts")) {
            TextView textView;
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 4 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            if(type.equalsIgnoreCase("daily")) {
                                text = "Goal Type: " + type + ", Target Hours: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            }
                            else{
                                text = "Goal Type: " + type + ", Target Days: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            }
                            textView = new TextView(this);
                            View itemLayout = getLayoutInflater().inflate(R.layout.activity_active_goals, null);
                            textView = itemLayout.findViewById(R.id.textView);
                            textView.setText(text);
                            goalContainer.addView(itemLayout);
                        }
                        inner_count++;
                    }
                }
            }
        }
    }
}
