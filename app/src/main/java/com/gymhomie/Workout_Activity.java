package com.gymhomie;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.workouts.AddWorkout;
import com.gymhomie.workouts.addExercise;

import java.util.HashMap;
import java.util.Map;

public class Workout_Activity extends AppCompatActivity {
    private static final String KEY_NAME = "Workout Name";
    private static final String KEY_MUSCLE_GROUPS = "Muscle Groups";
    private static final String KEY_EXERCISES = "Exercises";
    private EditText workoutName;
    private ToggleButton arms, legs, glutes, chest, back, cardio, other;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AddWorkout obj = new AddWorkout();

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

        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arms.isChecked()) {
                    obj.muscleGroup.add("Arms");
                } else {
                    obj.muscleGroup.remove("Arms");
                }
            }
        });

        legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (legs.isChecked()) {
                    obj.muscleGroup.add("Legs");
                } else {
                    obj.muscleGroup.remove("Legs");
                }
            }
        });

        glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (glutes.isChecked()) {
                    obj.muscleGroup.add("Glutes");
                } else {
                    obj.muscleGroup.remove("Glutes");
                }
            }
        });

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chest.isChecked()) {
                    obj.muscleGroup.add("Chest");
                } else {
                    obj.muscleGroup.remove("Chest");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (back.isChecked()) {
                    obj.muscleGroup.add("Back");
                } else {
                    obj.muscleGroup.remove("Back");
                }
            }
        });
        cardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardio.isChecked()) {
                    obj.muscleGroup.add("Cardio");
                } else {
                    obj.muscleGroup.remove("Cardio");
                }
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (other.isChecked()) {
                    obj.muscleGroup.add("Other");
                }
                else {
                    obj.muscleGroup.remove("Other");
                }
            }
        });


        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Workout_Activity.this, addExercise.class);
                startActivity(intent);
            }

        });
        //myWorkout.exercises.add(myExercise);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view, obj);
            }
        });

    }

    public void saveNote(View v, AddWorkout obj) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Workouts";
        String workout = obj.name;

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_NAME, workout);

        for(int i = 0; i < obj.muscleGroup.size(); i++)
        {
            note.put(KEY_MUSCLE_GROUPS, obj.muscleGroup);
        }

        for(int i = 0; i < obj.exercises.size(); i++)
        {
            note.put(KEY_EXERCISES, obj.exercises);
        }



        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Workout_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Workout_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
