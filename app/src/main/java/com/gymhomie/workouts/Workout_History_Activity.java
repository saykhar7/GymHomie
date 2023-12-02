package com.gymhomie.workouts;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

//when user clicks a date shown on the datepicker, database will search for
// a "completed_workout" item / or a workout id that is associated with that date
// if a workout is found on the selected date, it will display at the bottom of the screen
// either just the workout name or the whole workout with exercises

public class Workout_History_Activity extends AppCompatActivity {

    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_history);

        datePicker = findViewById(R.id.history_date_picker);

    }

    /*public void saveNote(View v, workout obj) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Workouts";

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
    }*/
}

