package com.gymhomie.workouts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.AchievementAdapter;
import com.gymhomie.R;
import com.gymhomie.tools.Achievement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//when user clicks a date shown on the datepicker, database will search for
// a "completed_workout" item / or a workout id that is associated with that date
// if a workout is found on the selected date, it will display at the bottom of the screen
// either just the workout name or the whole workout with exercises

public class Workout_History_Activity extends AppCompatActivity {

    DatePicker datePicker;
    RecyclerView workoutRecycler;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String userWorkoutHistoryPath = "users/" + userID + "/Workout History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_history);

        datePicker = findViewById(R.id.history_date_picker);
        workoutRecycler = findViewById(R.id.workout_history_recycler);

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                //make textviews and display the selected date to test values
                year = datePicker.getYear();
                month = datePicker.getMonth()+1;
                day = datePicker.getDayOfMonth();
                displayWorkout(month, day, year);

            }
        });

    }

    public void displayWorkout(int month, int day, int year) {
        ArrayList<workout> workoutList = new ArrayList<workout>();

        db.collection(userWorkoutHistoryPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            int workoutDay = ((Long) documentSnapshot.get("Day")).intValue();
                            int workoutMonth = ((Long) documentSnapshot.get("Month")).intValue();
                            int workoutYear = ((Long) documentSnapshot.get("Year")).intValue();
                            if(year == workoutYear && month == workoutMonth && day == workoutDay)
                            {
                                //String id = documentSnapshot.getId();
                                Map<String, Object> workoutData = (Map<String, Object>) documentSnapshot.get("Workout");
                                //String name = ((String) workoutData.get("name")).toString();
                                //ArrayList<String> muscleGroups = (ArrayList<String>) workoutData.get("muscleGroups");
                                ArrayList<exercise> exercisesData = (ArrayList<exercise>) workoutData.get("exercises");
                                ArrayList<exercise> exercises = new ArrayList<>();
                                if (exercisesData != null) {
                                    for (int i = 0; i < exercisesData.size(); i++) {
                                        Map<String, Object> exerciseData = (Map<String, Object>) exercisesData.get(i);
                                        exercise currentExercise = new exercise();
                                        currentExercise.setExerciseName((String) exerciseData.get("exerciseName"));
                                        currentExercise.setMinutes(((Long) exerciseData.get("minutes")).intValue());
                                        currentExercise.setNumReps(((Long) exerciseData.get("numReps")).intValue());
                                        currentExercise.setSeconds(((Long) exerciseData.get("seconds")).intValue());
                                        currentExercise.setNumSets(((Long) exerciseData.get("numSets")).intValue());
                                        currentExercise.setWeight(((Long) exerciseData.get("weight")).intValue());
                                        exercises.add(currentExercise);
                                    }
                                }
                                workout currentWorkout = new workout((String) workoutData.get("name"),
                                        (ArrayList<String>) workoutData.get("muscleGroups"), exercises);
                                workoutList.add(currentWorkout);
                            }
                            //else "no workout done"

                        }
                        // update UI
                        WorkoutAdapter adapter = new WorkoutAdapter(getApplicationContext(), workoutList);
                        workoutRecycler.setAdapter(adapter);
                        workoutRecycler.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Workout Viewing Retrieval", "Failure to retrieve user Workout History");
                    }
                });
    }
}

