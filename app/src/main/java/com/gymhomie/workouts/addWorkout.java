package com.gymhomie.workouts;

//adds workout to database

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gymhomie.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.Water_Intake_Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addWorkout extends AppCompatActivity {

    private static final String KEY_NAME = "Workout Name";
    private static final String KEY_MUSCLE_GROUPS = "Muscle Groups";
    private static final String KEY_EXERCISES = "Exercises";
    private EditText workoutName;
    private ToggleButton arms, legs, glutes, chest, back, cardio, other;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public workout myWorkout;
    public workout.exercise myExercise = new workout.exercise();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_workout);

        workoutName = findViewById(R.id.workout_name);

        arms = findViewById(R.id.arms_toggle);
        legs = findViewById(R.id.legs_toggle);
        glutes = findViewById(R.id.glutes_toggle);
        chest = findViewById(R.id.chest_toggle);
        back = findViewById(R.id.back_toggle);
        cardio = findViewById(R.id.cardio_toggle);
        other = findViewById(R.id.other_toggle);

        Button addExerciseButton = findViewById(R.id.add_exercise_button);
        Button finish = findViewById(R.id.finish_button);
        ArrayList<String> muscleGroup = new ArrayList<String>();

        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arms.isChecked()) {
                    muscleGroup.add("Arms");
                } else {
                    muscleGroup.remove("Arms");
                }
            }
        });

        legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (legs.isChecked()) {
                    muscleGroup.add("Legs");
                } else {
                    muscleGroup.remove("Legs");
                }
            }
        });

        glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (glutes.isChecked()) {
                    muscleGroup.add("Glutes");
                } else {
                    muscleGroup.remove("Glutes");
                }
            }
        });

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chest.isChecked()) {
                    muscleGroup.add("Chest");
                } else {
                    muscleGroup.remove("Chest");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (back.isChecked()) {
                    muscleGroup.add("Back");
                } else {
                    muscleGroup.remove("Back");
                }
            }
        });
        cardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardio.isChecked()) {
                    muscleGroup.add("Cardio");
                } else {
                    muscleGroup.remove("Cardio");
                }
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (other.isChecked()) {
                    muscleGroup.add("Other");
                }
                else {
                    muscleGroup.remove("Other");
                }
            }
        });


        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addWorkout.this, addExercise.class);
                startActivity(intent);
            }

        });
        //myWorkout.exercises.add(myExercise);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });

    }

    public void saveNote(View v) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Workouts";
        String workout = workoutName.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_NAME, workout);

        for(int i = 0; i < myWorkout.muscleGroups.size(); i++)
        {
            note.put(KEY_MUSCLE_GROUPS, myWorkout.muscleGroups);
        }

        for(int i = 0; i < myWorkout.exercises.size(); i++)
        {
            note.put(KEY_EXERCISES, myWorkout.exercises);
        }



        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addWorkout.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addWorkout.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
