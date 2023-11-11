package com.gymhomie.fragments;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.AchievementAdapter;
import com.gymhomie.R;
import com.gymhomie.WorkoutAdapter;
import com.gymhomie.Workout_Activity;
import com.gymhomie.tools.Achievement;
import com.gymhomie.workouts.exercise;
import com.gymhomie.workouts.workout;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;


public class workout_fragment extends Fragment {

    Button add_workout_button;
    Button workout_history_button;
    private ArrayList<workout> workoutList = new ArrayList<workout>();
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String userWorkoutsPath = "users/" + userID + "/Workouts";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        ArrayList<String> mg = new ArrayList<String>();
        mg.add("arms");
        exercise e = new exercise("e1", 3, 4, 34);
        workout w = new workout("hardcode", mg, )


        add_workout_button = view.findViewById(R.id.add_workout_button);
        recyclerView = view.findViewById(R.id.workout_recycler_view);

        listWorkouts();

        add_workout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Workout_Activity.class);
                startActivity(intent);
                //new addWorkout();
            }
        });

        return view;

    }
    private void listWorkouts()
    {
        db.collection(userWorkoutsPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    //check if workouts exist
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            if (!documentSnapshot.exists()){
                                continue;
                            }
                            Map<String, Object> workoutData = (Map<String, Object>) documentSnapshot.get("Workout");
                            ArrayList<exercise> exercisesData = (ArrayList<exercise>) workoutData.get("exercises");
                            ArrayList<exercise> exercises = new ArrayList<>();
                            if (exercisesData != null) {
                                for (int i = 0; i < exercisesData.size(); i++) {
                                    Map<String, Object> exerciseData = (Map<String, Object>) exercisesData.get(i);
                                    exercise currentExercise = new exercise();
                                    currentExercise.setExerciseName((String) exerciseData.get("exerciseName"));
                                    currentExercise.setMinutes((String) exerciseData.get("minutes"));
                                    currentExercise.setNumReps(((Long) exerciseData.get("numReps")).intValue());
                                    currentExercise.setSeconds((String) exerciseData.get("seconds"));
                                    currentExercise.setNumSets(((Long) exerciseData.get("numSets")).intValue());
                                    currentExercise.setWeight((String) exerciseData.get("weight"));
                                    exercises.add(currentExercise);
                                }
                            }
                            workout currentWorkout = new workout((String) workoutData.get("name"), (ArrayList<String>) workoutData.get("muscleGroups"), exercises);
                            workoutList.add(currentWorkout);
                        }
                        // update UI
                        WorkoutAdapter adapter = new WorkoutAdapter(getActivity(), workoutList);

                        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
                        recyclerView.setAdapter(adapter);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Workout Viewing Retrieval", "Failure to retrieve user Workouts");
                    }
                });
    }
}