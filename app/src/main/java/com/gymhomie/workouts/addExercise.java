package com.gymhomie.workouts;

import android.content.Intent;
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
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.gymhomie.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    MultiAutoCompleteTextView exerciseName;
    NumberPicker numSets, numReps, minutes, seconds, numWeight;
    Switch timed;
    TextView repsText, minutesText, secondsText;
    Button saveExercise;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isTimed;
    exercise e;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_exercise);

        e = new exercise();

        exerciseName = findViewById(R.id.exercise_name);
        numSets = findViewById(R.id.numSets);
        numReps = findViewById(R.id.numReps);
        numWeight = findViewById(R.id.weight);
        timed = findViewById(R.id.time_switch);
        saveExercise = findViewById(R.id.save_exercise_button);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);
        repsText = findViewById(R.id.numreps_text);
        minutesText = findViewById(R.id.minutes_text);
        secondsText = findViewById(R.id.seconds_text);


        numSets.setMinValue(1);
        numSets.setMaxValue(20);
        numSets.setValue(3);

        numReps.setMinValue(1);
        numReps.setMaxValue(30);
        numReps.setValue(10);

        numWeight.setMinValue(0);
        numWeight.setMaxValue(1000);
        numWeight.setValue(10);

        minutes.setMinValue(0);
        minutes.setMaxValue(300);
        minutes.setValue(0);

        seconds.setMinValue(0);
        seconds.setMaxValue(59);
        seconds.setValue(10);


        timed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.v("Switch State=", ""+isChecked);
                if (isChecked)
                {
                    isTimed = true;
                    e.setTimed(true);
                    minutes.setVisibility(View.VISIBLE);
                    seconds.setVisibility(View.VISIBLE);
                    minutesText.setVisibility(View.VISIBLE);
                    secondsText.setVisibility(View.VISIBLE);
                    numReps.setVisibility(View.GONE);
                    repsText.setVisibility(View.GONE);
                }
                else
                {
                    isTimed = false;
                    e.setTimed(false);
                    minutes.setVisibility(View.GONE);
                    seconds.setVisibility(View.GONE);
                    minutesText.setVisibility(View.GONE);
                    secondsText.setVisibility(View.GONE);
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

                e.setExerciseName(exerciseName.getText().toString());
                e.setNumSets(numSets.getValue());
                e.setWeight(numWeight.getValue());
                if(isTimed) {
                    e.setSeconds(seconds.getValue());
                    e.setMinutes(minutes.getValue());
                    e.setTimed(true);
                }
                else{
                    e.setNumReps(numReps.getValue());
                    e.setTimed(false);
                }


                Intent resultIntent = new Intent();
                resultIntent.putExtra("newExercise", e);
                Log.d("addExercise name", e.getExerciseName());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private static final String[] EXERCISES = new String[]{
            "Bicep Curl", "Lateral Raise", "Crunch", "RDL", "Goblet Squat"
    };

}


