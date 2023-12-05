package com.gymhomie.workouts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

public class Workout_Activity extends AppCompatActivity {
    private static final String KEY_NAME = "Workout Name";
    private static final String KEY_MUSCLE_GROUPS = "Muscle Groups";
    private static final String KEY_EXERCISES = "Exercises";
    private static final int REQUEST_CODE_CUSTOMIZE_EXERCISE = 99;

    private EditText workoutName;
    private ToggleButton arms, legs, glutes, chest, back, cardio, other;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private workout obj = new workout();

    private ViewModel viewModel;
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

        obj.setName(workoutName.toString());


        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arms.isChecked()) {
                    obj.addMG("Arms");
                } else {
                    obj.removeMG("Arms");
                }
            }
        });

        legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (legs.isChecked()) {
                    obj.addMG("Legs");
                } else {
                    obj.removeMG("Legs");
                }
            }
        });

        glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (glutes.isChecked()) {
                    obj.addMG("Glutes");
                } else {
                    obj.removeMG("Glutes");
                }
            }
        });

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chest.isChecked()) {
                    obj.addMG("Chest");
                } else {
                    obj.removeMG("Chest");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (back.isChecked()) {
                    obj.addMG("Back");
                } else {
                    obj.removeMG("Back");
                }
            }
        });
        cardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardio.isChecked()) {
                    obj.addMG("Cardio");
                } else {
                    obj.removeMG("Cardio");
                }
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (other.isChecked()) {
                    obj.addMG("Other");
                }
                else {
                    obj.removeMG("Other");
                }
            }
        });


        final String[] EXERCISES = new String[]{
                "Bicep Curl", "Lateral Raise", "Crunch", "RDL", "Goblet Squat"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, EXERCISES);

        //button listener that changes the view to addExercise
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Workout_Activity.this, addExercise.class);
                startActivityForResult(intent, REQUEST_CODE_CUSTOMIZE_EXERCISE);

            }
        });

        //button listener to save the workout to the user profile and database
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveNote(view, obj);
                finish();
            }
        });
    }

    // saves workout in the database in the Workouts collection with the
    // workoutName as the name of the collection
    public void saveNote(View v, workout obj) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Workouts";

        obj.setName(workoutName.getText().toString());
        workout newWorkout = new workout(obj.getName(), obj.getMuscleGroups(), obj.getExercises());

        Map<String, Object> note = new HashMap<>();
        note.put("Workout", newWorkout);

        db.collection(collectionPath).document(workoutName.getText().toString()).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Workout_Activity.this, "Note saved: ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Workout_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CUSTOMIZE_EXERCISE && resultCode == RESULT_OK) {
            exercise newExercise = data.getParcelableExtra("newExercise");
            if (newExercise != null) {
                obj.addExercise(newExercise);
            }
            else {
                Log.e("Workout Activity addExercise", "Got null new exercise");
            }
            // Use the exercise object in your Workout Activity
        }
    }
}
