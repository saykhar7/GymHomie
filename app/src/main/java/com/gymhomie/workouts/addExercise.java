package com.gymhomie.workouts;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
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

public class addExercise extends AppCompatActivity{


    private MultiAutoCompleteTextView exerciseName;
    private NumberPicker numSets, numReps, numWeight;
    private Switch timed;

    private Button saveExercise;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);

        exerciseName = findViewById(R.id.exercise_name);
        numSets = findViewById(R.id.numSets);
        numReps = findViewById(R.id.numReps);
        numWeight = findViewById(R.id.weight);
        timed = findViewById(R.id.time_switch);
        saveExercise = findViewById(R.id.save_exercise_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        exerciseName.setAdapter(adapter);
        exerciseName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    exercise myExercise = new exercise (exerciseName, numSets, numReps, numWeight);

    private static final String[] COUNTRIES = new String[] {
            "Bicep Curl", " ", "", "", ""
    };

    public void saveNote(View v) {

        int sets = numSets.getValue();
        int reps = numReps.getValue();
        int weight = numWeight.getValue();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+this.getName()+"/exercises";


        Map<String, Object> note = new HashMap<>();
        note.put(KEY_YEAR, year);
        note.put(KEY_MONTH, month);
        note.put(KEY_DAY, day);
        note.put(KEY_AMOUNT, amount);

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


