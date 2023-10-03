package com.gymhomie;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gymhomie.calculator.CaloriesBurned;
import com.gymhomie.R;

import org.json.JSONException;

public class CaloriesCalculator_Activity extends AppCompatActivity {

    EditText exerciseEditText;
    EditText heightFeetEditText;
    EditText heightInchesEditText;
    EditText weightEditText;
    EditText genderEditText;
    EditText ageEditText;
    EditText durationEditText;
    Button calculateButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caloriescalculator);
        exerciseEditText = findViewById(R.id.exercise);
        heightFeetEditText = findViewById(R.id.heightFeet);
        heightInchesEditText = findViewById(R.id.heightInches);
        weightEditText = findViewById(R.id.weight);
        genderEditText = findViewById(R.id.gender);
        ageEditText = findViewById(R.id.age);
        durationEditText = findViewById(R.id.duration);
        calculateButton = findViewById(R.id.calculateButton);

        // Set a click listener for the Calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input from EditText fields
                String exercise = exerciseEditText.getText().toString().trim();
                String heightFeet = heightFeetEditText.getText().toString().trim();
                String heightInches = heightInchesEditText.getText().toString().trim();
                String weight = weightEditText.getText().toString().trim();
                String gender = genderEditText.getText().toString().trim();
                String age = ageEditText.getText().toString().trim();
                String duration = durationEditText.getText().toString().trim();

                // Check if any field is empty
                if (exercise.isEmpty() || heightFeet.isEmpty() || heightInches.isEmpty() || weight.isEmpty() || gender.isEmpty() || age.isEmpty() || duration.isEmpty()) {
                    // Display an exception message indicating which fields are required
                    StringBuilder message = new StringBuilder("Please fill out the following fields:\n");
                    if (exercise.isEmpty()) {
                        message.append("- Exercise\n");
                    }
                    if (heightFeet.isEmpty()) {
                        message.append("- New Field\n");
                    }
                    if (heightInches.isEmpty()) {
                        message.append("- Height\n");
                    }
                    if (weight.isEmpty()) {
                        message.append("- Weight\n");
                    }
                    if (gender.isEmpty()) {
                        message.append("- Gender\n");
                    }
                    if (age.isEmpty()) {
                        message.append("- Age\n");
                    }
                    if (duration.isEmpty()) {
                        message.append("- Duration\n");
                    }

                    // Show the exception message as a toast
                    Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
                } else {
                    // All fields are filled, you can proceed with your calculations
                    new CalculateTask().execute(exercise, heightFeet, heightInches, weight, gender, age, duration);
                }
            }
        });
    }

    private class CalculateTask extends AsyncTask<String, Void, Double> {
        @Override
        protected Double doInBackground(String... params) {
            String exercise = params[0];
            double heightFeet = Double.parseDouble(params[1]);
            double heightInches = Double.parseDouble(params[2]);
            double weight = Double.parseDouble(params[3]);
            String gender = params[4];
            int age = Integer.parseInt(params[5]);
            double duration = Double.parseDouble(params[6]);

            // Perform your calculation logic here
            CaloriesBurned cb = new CaloriesBurned(exercise, heightFeet, weight, age, heightInches, gender, duration);
            double caloriesBurned = 0;
            double respDuration = 0;

            try {
                String response = cb.calculate();
                caloriesBurned = cb.getCaloriesBurned(response);
                respDuration = cb.getDuration(response);
                double ratio = 0;
                // ratio = expected value / observed value
                ratio = duration / respDuration;
                // final calories burned estimate calc
                caloriesBurned = ratio * caloriesBurned;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return caloriesBurned;
        }

        @Override
        protected void onPostExecute(Double caloriesBurned) {
            // Update the UI with the result
            setContentView(R.layout.calories_burned_result);
            TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
            caloriesBurnedTextView.setText(String.format("%.2f", caloriesBurned));
        }
    }
}
