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

import java.util.ArrayList;


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
                            String name = (String) documentSnapshot.get("name");
                            ArrayList<String> mg = (ArrayList<String>) documentSnapshot.get("muscleGroups");
                            ArrayList<exercise> exercises = (ArrayList<exercise>) documentSnapshot.get("exercises");

                            workout currentWorkout = new workout(name, mg, exercises);
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