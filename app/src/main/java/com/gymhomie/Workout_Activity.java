package com.gymhomie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
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
import com.gymhomie.workouts.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Workout_Activity extends AppCompatActivity {
    private static final String KEY_NAME = "Workout Name";
    private static final String KEY_MUSCLE_GROUPS = "Muscle Groups";
    private static final String KEY_EXERCISES = "Exercises";

    private EditText workoutName;
    private ToggleButton arms, legs, glutes, chest, back, cardio, other;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private workout obj = new workout();

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
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultiAutoCompleteTextView exerciseName;
                NumberPicker numSets, numReps;
                EditText numWeight, time;
                Switch timed;
                TextView seconds, repsText;
                Button saveExercise;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //String workoutName;
                final boolean[] isTimed = new boolean[1];

                setContentView(R.layout.add_exercise);

                exerciseName = findViewById(R.id.exercise_name);
                numSets = findViewById(R.id.numSets);
                numReps = findViewById(R.id.numReps);
                numWeight = findViewById(R.id.weight);
                timed = findViewById(R.id.time_switch);
                saveExercise = findViewById(R.id.save_exercise_button);
                time = findViewById(R.id.time);
                seconds = findViewById(R.id.seconds_text);
                repsText = findViewById(R.id.numreps_text);
                numSets.setMinValue(1);
                numSets.setMaxValue(20);
                numSets.setValue(3);

                numReps.setMinValue(1);
                numReps.setMaxValue(30);
                numReps.setValue(10);



                timed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Log.v("Switch State=", ""+isChecked);
                        if (isChecked)
                        {
                            isTimed[0] = true;
                            time.setVisibility(View.VISIBLE);
                            seconds.setVisibility(View.VISIBLE);
                            numReps.setVisibility(View.GONE);
                            repsText.setVisibility(View.GONE);
                        }
                        else
                        {
                            isTimed[0] = false;
                            time.setVisibility(View.GONE);
                            seconds.setVisibility(View.GONE);
                            numReps.setVisibility(View.VISIBLE);
                            repsText.setVisibility(View.VISIBLE);
                        }
                    }
                });

                exerciseName.setAdapter(adapter);
                exerciseName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                saveExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        workout.exercise e = new workout.exercise(exerciseName, numSets, numReps, numWeight);
                        obj.addExercise(e);
                        //finish();
                        setContentView(R.layout.add_workout);
                    }
                });
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveNote(view, obj);
            }
        });
        if (savedInstanceState != null)
        {
            String savedWorkoutName = savedInstanceState.getString(KEY_NAME);
            workoutName.setText(savedWorkoutName);

            //ArrayList<String> savedMuscles = savedInstanceState.getStringArrayList(KEY_MUSCLE_GROUPS);

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate) {

        outstate.putString(KEY_NAME, workoutName.getText().toString());
        super.onSaveInstanceState(outstate);
    }
    public void saveNote(View v, workout obj) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Workouts";

        obj.setName(workoutName.getText().toString());
        workout newWorkout = new workout(obj.getName(), obj.getMuscleGroups(), obj.getExercises());

        Map<String, Object> note = new HashMap<>();
        note.put("Workout", newWorkout);

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
