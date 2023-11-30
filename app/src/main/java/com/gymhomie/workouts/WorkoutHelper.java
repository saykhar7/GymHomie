package com.gymhomie.workouts;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class WorkoutHelper {
    ArrayList<Map<String, Object>> workouts;
    ArrayList<Map<String, Object>> homieWorkouts;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String userWorkoutsPath = "users/" + userID + "/Workouts";

    public WorkoutHelper(){
        setWorkouts();
    }
    public void setWorkouts(){
        workouts = new ArrayList<>();
        // Assuming "workouts" is the name of your collection
        db.collection(userWorkoutsPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Iterate through the documents and extract workout details
                            //ArrayList<Map<String, Object>> workouts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming "Workout" is your model class
                                workouts.add((Map<String, Object>) document.get("Workout"));
                            }
                            //this.workouts = workouts;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public ArrayList<Map<String, Object>> getWorkouts() {
        return workouts;
    }

    public ArrayList<Map<String, Object>> getHomieWorkouts() {
        return homieWorkouts;
    }

    public void setHomieWorkouts(String homiePath) {
        homieWorkouts = new ArrayList<>();
        // Assuming "workouts" is the name of your collection
        db.collection(homiePath.substring(0, homiePath.length()-1) + "/Workouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Iterate through the documents and extract workout details
                            //ArrayList<Map<String, Object>> workouts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming "Workout" is your model class
                                homieWorkouts.add((Map<String, Object>) document.get("Workout"));
                            }
                            //this.workouts = workouts;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
