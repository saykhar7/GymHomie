package com.gymhomie;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.calculator.CaloriesBurned;
import com.gymhomie.R;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaloriesCalculator_Activity extends AppCompatActivity {

    EditText exerciseEditText;
    EditText heightFeetEditText;
    EditText heightInchesEditText;
    EditText weightEditText;
    EditText genderEditText;
    EditText ageEditText;
    EditText durationEditText;
    Button calculateButton;
    Button saveActivityButton;
    String exercise;
    String heightFeet;
    String heightInches;
    String weight;
    String gender;
    String age;
    String duration;
    Double caloriesBurned;

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
        saveActivityButton = findViewById(R.id.saveActivityButton);

        // Set a click listener for the Calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input from EditText fields
                exercise = exerciseEditText.getText().toString().trim();
                heightFeet = heightFeetEditText.getText().toString().trim();
                heightInches = heightInchesEditText.getText().toString().trim();
                weight = weightEditText.getText().toString().trim();
                gender = genderEditText.getText().toString().trim();
                age = ageEditText.getText().toString().trim();
                duration = durationEditText.getText().toString().trim();

                // Check if any field is empty
                if (exercise.isEmpty() || heightFeet.isEmpty() || heightInches.isEmpty() || weight.isEmpty() || gender.isEmpty() || age.isEmpty() || duration.isEmpty()) {
                    // Display an exception message indicating which fields are required
                    StringBuilder message = new StringBuilder("Please fill out the following fields:\n");
                    if (exercise.isEmpty()) {
                        message.append("- Exercise (ex: \"ran 3 miles\")\n");
                    }
                    if (heightFeet.isEmpty()) {
                        message.append("- Height (feet)\n");
                    }
                    if (heightInches.isEmpty()) {
                        message.append("- Height (inches)\n");
                    }
                    if (weight.isEmpty()) {
                        message.append("- Weight (lbs)\n");
                    }
                    if (gender.isEmpty()) {
                        message.append("- Gender (male or female)\n");
                    }
                    if (age.isEmpty()) {
                        message.append("- Age (no decimals)\n");
                    }
                    if (duration.isEmpty()) {
                        message.append("- Duration (in minutes)\n");
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
    public void setCaloriesBurned(Double caloriesBurned){
        this.caloriesBurned = caloriesBurned;
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
                caloriesBurned = CaloriesBurned.getCaloriesBurned(response);
                respDuration = CaloriesBurned.getDuration(response);
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
            setCaloriesBurned(caloriesBurned(caloriesBurned));
            saveActivityButton = findViewById(R.id.saveActivityButton);
            saveActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Send the stepCount and date to Firestore
                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);

                    // Extract the month, day, and year as numbers
                    // Use int instead of string to save space in DB
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based, so add 1
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    Map<String, Object> data = new HashMap<>();
                    data.put("exercise", exercise); // string
                    data.put("duration", Float.parseFloat(duration)); // float
                    data.put("calories", caloriesBurned); // float
                    data.put("day", day);
                    data.put("month", month);
                    data.put("year", year);

                    // Initiate Firestore instance and authorization
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    String newDocumentId = firestore.collection("users")
                            .document(auth.getUid())
                            .collection("CaloriesBurnedActivity")
                            .document().getId();
                    //  This block enters the user's step count data for the day.
                    firestore.collection("users")
                            .document(auth.getUid())
                            .collection("CaloriesBurnedActivity")
                            .document(newDocumentId)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener< Void >() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Data sent successfully
                                    Toast.makeText(CaloriesCalculator_Activity.this, "Congratulations on your activity! Your activity has been recorded in the database.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                }
                            });
                }
            });
        }
        public Double caloriesBurned(Double caloriesBurned){
            return caloriesBurned;
        }
    }
}
