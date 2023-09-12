package com.gymhomie.workouts;

//adds workout to database

import android.os.Bundle;
import android.view.View;
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

    private EditText workoutName;
    private ToggleButton arms, legs, glutes, chest, back, cardio, other;
    private Button addExerciseButton, finish;
    private FirebaseFirestore firebaseFirestore;



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

        addExerciseButton = findViewById(R.id.add_exercise_button);
        finish = findViewById(R.id.finish_button);

        ArrayList<String> muscleGroup = null;
        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> muscleGroup;
                //TODO: add to list of muscle group; do for each toggle button
            }
            public void onToggleCLick(View view)
            {
                if (arms.isChecked())
                {
                    muscleGroup.add("Arms");
                }
                else if(legs.isChecked())
                {
                    muscleGroup.add("Legs");
                }
                else if(glutes.isChecked())
                {
                    muscleGroup.add("Glutes");
                }
                else if(chest.isChecked())
                {
                    muscleGroup.add("Chest");
                }
                else if(back.isChecked())
                {
                    muscleGroup.add("Back");
                }
                else if(cardio.isChecked())
                {
                    muscleGroup.add("Cardio");
                }
                else
                {
                    muscleGroup.add("Other");
                }
            }
        });

        ArrayList<exercise> exercises = null;
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new addExercise();
            }
        });

        workout myWorkout = new workout(workoutName, muscleGroup, exercises);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });

    }

    public void saveNote(View v) {

        int amount = amountPicker.getValue();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"Workouts";


        Map<String, Object> note = new HashMap<>();
        note.put(KEY_YEAR, year);
        note.put(KEY_MONTH, month);
        note.put(KEY_DAY, day);
        note.put(KEY_AMOUNT, amount);

        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Water_Intake_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Water_Intake_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
