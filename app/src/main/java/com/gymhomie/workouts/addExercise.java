package com.gymhomie.workouts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gymhomie.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.Water_Intake_Activity;

import java.util.HashMap;
import java.util.Map;


/*
    can't figure out how to get workout name to reference in order to put the exercises in the db
    inside of the right workout
*/
public class addExercise extends AppCompatActivity{
    private static final String KEY_SETS = "Number of Sets";
    private static final String KEY_REPS = "Number of Reps";
    private static final String KEY_WEIGHT = "Amount of Weight";

    addWorkout w = new addWorkout();
    MultiAutoCompleteTextView exerciseName;
    NumberPicker numSets, numReps;
    EditText numWeight, time;
    Switch timed;
    TextView seconds, repsText;
    Button saveExercise;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //String workoutName;


    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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
                    time.setVisibility(View.VISIBLE);
                    seconds.setVisibility(View.VISIBLE);
                    numReps.setVisibility(View.GONE);
                    repsText.setVisibility(View.GONE);
                }
                else
                {
                    time.setVisibility(View.GONE);
                    seconds.setVisibility(View.GONE);
                    numReps.setVisibility(View.VISIBLE);
                    repsText.setVisibility(View.VISIBLE);
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, EXERCISES);
        exerciseName.setAdapter(adapter);
        exerciseName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                w.myExercise.seconds = time;
                w.myExercise.exerciseName = exerciseName;
                w.myExercise.numSets = numSets;
                w.myExercise.numReps = numReps;
                w.myExercise.weight = numWeight;
                //saveNote(view);
                w.myWorkout.exercises.set(-1, w.myExercise);
            }
        });
    }


    private static final String[] EXERCISES = new String[]{
            "Bicep Curl", "Lateral Raise", "Crunch", "RDL", "Goblet Squat"
    };
        public void saveNote (View v){

            int sets = numSets.getValue();
            int reps = numReps.getValue();
            String weight = numWeight.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userID = auth.getCurrentUser().getUid();
            String collectionPath = "users/" + userID + "/Workouts" + "/Exercises";


            Map<String, Object> note = new HashMap<>();
            note.put(KEY_SETS, sets);
            note.put(KEY_REPS, reps);
            note.put(KEY_WEIGHT, weight);

            db.collection(collectionPath).document().set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(addExercise.this, "Note saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addExercise.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
}


