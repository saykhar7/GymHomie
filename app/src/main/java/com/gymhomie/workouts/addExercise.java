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

    //workout w = new workout();
    MultiAutoCompleteTextView exerciseName;
    NumberPicker numSets, numReps;
    EditText numWeight, time;
    Switch timed;
    TextView seconds, repsText;
    Button saveExercise;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //String workoutName;
    boolean isTimed;

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
                    isTimed = true;
                    time.setVisibility(View.VISIBLE);
                    seconds.setVisibility(View.VISIBLE);
                    numReps.setVisibility(View.GONE);
                    repsText.setVisibility(View.GONE);
                }
                else
                {
                    isTimed = false;
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
            //workout.exercise e = new workout.exercise(exerciseName, numSets, numReps, numWeight);
            @Override
            public void onClick(View view) {
                workout.exercise e = new workout.exercise(exerciseName, numSets, numReps, numWeight);
/*
                e.setName(exerciseName);
                e.setNumReps(numReps);
                e.setWeight(numWeight);
                if(isTimed) {
                    e.setNumSets(numSets);
                    /*w.exercises.get(0).seconds = time;
                    w.exercises.get(0).exerciseName = exerciseName;
                    w.exercises.get(0).numSets = numSets;
                    w.exercises.get(0).numReps = numReps;
                    w.exercises.get(0).weight = numWeight;
                    //saveNote(view);
                }
                else{
                    e.setSeconds(time);
                }
                workout w = new workout();
                //w.myWorkout.exercises.set(-1, w.exercises.get(0));*/
                //  workout w = new workout();
                //w.myWorkout.exercises.set(-1, e);

                //  w.myWorkout.addexercise(e);
            }
        });
    }


    private static final String[] EXERCISES = new String[]{
            "Bicep Curl", "Lateral Raise", "Crunch", "RDL", "Goblet Squat"
    };

}


