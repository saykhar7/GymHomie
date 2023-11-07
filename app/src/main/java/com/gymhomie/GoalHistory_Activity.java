package com.gymhomie;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gymhomie.tools.Goal;

public class GoalHistory_Activity extends AppCompatActivity {
    private final Goal goal = new Goal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_goals);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);


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
            tv.setText("Step Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetStepsHeader = createHeaderTextView("Target Steps");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
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
                            TableRow row = new TableRow(this);
                            target = getIntent().getStringExtra(String.valueOf(count));
//                            text = "Goal Type: " + type + ", Target Steps: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
//                            textView = new TextView(this);
                            // Create TextViews for each column in the header row
                            TextView _type_ = createHeaderTextView(type);
                            TextView targetSteps = createHeaderTextView(target);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(targetSteps);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("hydration")) {
            tv.setText("Hydration Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetStepsHeader = createHeaderTextView("Target Ounces");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
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
                            //text = "Goal Type: " + type + ", Target Ounces: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            TableRow row = new TableRow(this);
                            TextView _type_ = createHeaderTextView(type);
                            TextView targetOunces = createHeaderTextView(target);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(targetOunces);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("weight")) {
            TextView textView;
            tv.setText("Step Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView targetStepsHeader = createHeaderTextView("Target Weight");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
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
                            TableRow row = new TableRow(this);
                            TextView targetWeight = createHeaderTextView(target);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(targetWeight);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("exercise")) {
            tv.setText("Exercise Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Exercise");
            TextView targetStepsHeader = createHeaderTextView("Target");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
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
                            //text = "Exercise Type: " + type + ", Target: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            TableRow row = new TableRow(this);
                            TextView _type_ = createHeaderTextView(type);
                            TextView max_target = createHeaderTextView(target);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(max_target);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("workout")) {
            tv.setText("Workout Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetStepsHeader = createHeaderTextView("Target");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
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
                                TableRow row = new TableRow(this);
                                TextView _type_ = createHeaderTextView(type);
                                TextView target_streak = createHeaderTextView(target + "h");
                                TextView startDate = createHeaderTextView(start_date);
                                TextView endDate= createHeaderTextView(end_date);

                                // Add the TextViews to the header row
                                row.addView(_type_);
                                row.addView(target_streak);
                                row.addView(startDate);
                                row.addView(endDate);

                                // Add the header row to the table
                                tableLayout.addView(row);
                            }
                            else{
                                TableRow row = new TableRow(this);
                                TextView _type_ = createHeaderTextView(type);
                                TextView target_streak = createHeaderTextView(target + "d");
                                TextView startDate = createHeaderTextView(start_date);
                                TextView endDate= createHeaderTextView(end_date);

                                // Add the TextViews to the header row
                                row.addView(_type_);
                                row.addView(target_streak);
                                row.addView(startDate);
                                row.addView(endDate);

                                // Add the header row to the table
                                tableLayout.addView(row);
                            }
                        }
                        inner_count++;
                    }
                }
            }
        }
    }
    // Helper method to create a header TextView
    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        //textView.setBackgroundResource(R.color.header_background); // Add a background color
        // Customize other attributes as needed
        return textView;
    }

    // Helper method to create a data TextView
    private TextView createDataTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        // Customize other attributes as needed
        return textView;
    }
}