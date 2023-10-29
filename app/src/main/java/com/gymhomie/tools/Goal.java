package com.gymhomie.tools;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Goal {
    // Initialize Firestore instance and authentication
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String user_id;
    private ArrayList<Boolean> goalCollection;
    private ArrayList<Map<String, Object>> stepGoals;
    private ArrayList<Map<String, Object>> hydrationGoals;
    private ArrayList<Map<String, Object>> weightGoals;
    private ArrayList<Map<String, Object>> exerciseGoals;

    public ArrayList<Map<String, Object>> getStepGoals() {
        return stepGoals;
    }

    public void setStepGoals(ArrayList<Map<String, Object>> stepGoals) {
        this.stepGoals = stepGoals;
    }

    public ArrayList<Map<String, Object>> getHydrationGoals() {
        return hydrationGoals;
    }

    public void setHydrationGoals(ArrayList<Map<String, Object>> hydrationGoals) {
        this.hydrationGoals = hydrationGoals;
    }

    public ArrayList<Map<String, Object>> getWeightGoals() {
        return weightGoals;
    }

    public void setWeightGoals(ArrayList<Map<String, Object>> weightGoals) {
        this.weightGoals = weightGoals;
    }

    public ArrayList<Map<String, Object>> getExerciseGoals() {
        return exerciseGoals;
    }

    public void setExerciseGoals(ArrayList<Map<String, Object>> exerciseGoals) {
        this.exerciseGoals = exerciseGoals;
    }

    public ArrayList<Map<String, Object>> getWorkoutGoals() {
        return workoutGoals;
    }

    public void setWorkoutGoals(ArrayList<Map<String, Object>> workoutGoals) {
        this.workoutGoals = workoutGoals;
    }

    private ArrayList<Map<String, Object>> workoutGoals;

    public ArrayList<Boolean> getGoalCollection() {
        return goalCollection;
    }

    public void setGoalCollection(ArrayList<Boolean> goalCollection) {
        this.goalCollection = goalCollection;
    }

    public Goal() {
        goalCollection = new ArrayList<>();
        stepGoals = new ArrayList<>();
        hydrationGoals = new ArrayList<>();
        weightGoals = new ArrayList<>();
        exerciseGoals = new ArrayList<>();
        workoutGoals = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getUid();
        String[] collectionPaths = {
                "users/" + user_id + "/StepCounter",
                "users/" + user_id + "/WaterIntakes",
                "users/" + user_id + "/Profile",
                "users/" + user_id + "/Exercise",
                "users/" + user_id + "/Workouts"
        };
        setCollections(collectionPaths);
        queryStepCounterGoals();
        queryWorkoutGoals();
        queryExerciseGoals();
        queryWeightGoals();
        queryHydrationGoals();
    }
    public boolean hasGoals() {
        return true;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    public void setFirestore(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void queryStepCounterGoals(){
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        if (title != null && title.equals("steps")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            stepGoals.add(documentData);
                        }
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }
    public void queryHydrationGoals(){
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        if (title != null && title.equals("hydration")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            hydrationGoals.add(documentData);
                        }
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }
    public void queryWeightGoals(){
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        if (title != null && title.equals("weight")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            weightGoals.add(documentData);
                        }
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }
    public void queryExerciseGoals(){
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        if (title != null && title.equals("exercise")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            exerciseGoals.add(documentData);
                        }
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }
    public void queryWorkoutGoals(){
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        if (title != null && title.equals("workout")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            workoutGoals.add(documentData);
                        }
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }

    public void setCollections(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            CollectionReference colRef = FirebaseFirestore.getInstance().collection(paths[i]);

            Query query = colRef.orderBy(FieldPath.documentId(), Query.Direction.ASCENDING).limit(1);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // The collection at the specified path exists and is not empty
                            goalCollection.add(true);
                        } else {
                            // The collection does not exist at the specified path or is empty
                            goalCollection.add(false);
                        }
                    } else {
                        // Handle the error
                        Exception e = task.getException();
                        if (e != null) {
                            // Handle the exception
                        }
                    }
                }
            });
        }
    }

}