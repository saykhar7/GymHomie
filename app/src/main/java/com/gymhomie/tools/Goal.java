package com.gymhomie.tools;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class Goal {
    // Initialize Firestore instance and authentication
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public ArrayList<Boolean> getCompletedGoalCollection() {
        return completedGoalCollection;
    }

    public void setCompletedGoalCollection(ArrayList<Boolean> completedGoalCollection) {
        this.completedGoalCollection = completedGoalCollection;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public ArrayList<Map<String, Object>> getNonActiveStepGoals() {
        return nonActiveStepGoals;
    }

    public void setNonActiveStepGoals(ArrayList<Map<String, Object>> nonActiveStepGoals) {
        this.nonActiveStepGoals = nonActiveStepGoals;
    }

    public ArrayList<Map<String, Object>> getCompletedStepGoals() {
        return completedStepGoals;
    }

    public void setCompletedStepGoals(ArrayList<Map<String, Object>> completedStepGoals) {
        this.completedStepGoals = completedStepGoals;
    }

    public ArrayList<Map<String, Object>> getNonActiveHydrationGoals() {
        return nonActiveHydrationGoals;
    }

    public void setNonActiveHydrationGoals(ArrayList<Map<String, Object>> nonActiveHydrationGoals) {
        this.nonActiveHydrationGoals = nonActiveHydrationGoals;
    }

    public ArrayList<Map<String, Object>> getCompletedHydrationGoals() {
        return completedHydrationGoals;
    }

    public void setCompletedHydrationGoals(ArrayList<Map<String, Object>> completedHydrationGoals) {
        this.completedHydrationGoals = completedHydrationGoals;
    }

    public ArrayList<Map<String, Object>> getNonActiveWeightGoals() {
        return nonActiveWeightGoals;
    }

    public void setNonActiveWeightGoals(ArrayList<Map<String, Object>> nonActiveWeightGoals) {
        this.nonActiveWeightGoals = nonActiveWeightGoals;
    }

    public ArrayList<Map<String, Object>> getCompletedWeightGoals() {
        return completedWeightGoals;
    }

    public void setCompletedWeightGoals(ArrayList<Map<String, Object>> completedWeightGoals) {
        this.completedWeightGoals = completedWeightGoals;
    }

    public ArrayList<Map<String, Object>> getNonActiveExerciseGoals() {
        return nonActiveExerciseGoals;
    }

    public void setNonActiveExerciseGoals(ArrayList<Map<String, Object>> nonActiveExerciseGoals) {
        this.nonActiveExerciseGoals = nonActiveExerciseGoals;
    }

    public ArrayList<Map<String, Object>> getCompletedExerciseGoals() {
        return completedExerciseGoals;
    }

    public void setCompletedExerciseGoals(ArrayList<Map<String, Object>> completedExerciseGoals) {
        this.completedExerciseGoals = completedExerciseGoals;
    }

    public ArrayList<Map<String, Object>> getNonActiveWorkoutGoals() {
        return nonActiveWorkoutGoals;
    }

    public void setNonActiveWorkoutGoals(ArrayList<Map<String, Object>> nonActiveWorkoutGoals) {
        this.nonActiveWorkoutGoals = nonActiveWorkoutGoals;
    }

    public ArrayList<Map<String, Object>> getCompletedWorkoutGoals() {
        return completedWorkoutGoals;
    }

    public void setCompletedWorkoutGoals(ArrayList<Map<String, Object>> completedWorkoutGoals) {
        this.completedWorkoutGoals = completedWorkoutGoals;
    }

    private String user_id;
    private ArrayList<Boolean> goalCollection;
    private ArrayList<Boolean> completedGoalCollection;
    private String current_status;
    private ArrayList<Map<String, Object>> activeStepGoals;
    private ArrayList<Map<String, Object>> nonActiveStepGoals;
    private ArrayList<Map<String, Object>> completedStepGoals;
    private ArrayList<Map<String, Object>> activeHydrationGoals;
    private ArrayList<Map<String, Object>> nonActiveHydrationGoals;
    private ArrayList<Map<String, Object>> completedHydrationGoals;
    private ArrayList<Map<String, Object>> activeWeightGoals;
    private ArrayList<Map<String, Object>> nonActiveWeightGoals;
    private ArrayList<Map<String, Object>> completedWeightGoals;
    private ArrayList<Map<String, Object>> activeExerciseGoals;
    private ArrayList<Map<String, Object>> nonActiveExerciseGoals;
    private ArrayList<Map<String, Object>> completedExerciseGoals;
    public ArrayList<Map<String, Object>> getStepGoals() {
        return activeStepGoals;
    }

    public void setStepGoals(ArrayList<Map<String, Object>> stepGoals) {
        this.activeStepGoals = stepGoals;
    }

    public ArrayList<Map<String, Object>> getHydrationGoals() {
        return activeHydrationGoals;
    }

    public void setHydrationGoals(ArrayList<Map<String, Object>> hydrationGoals) {
        this.activeHydrationGoals = hydrationGoals;
    }

    public ArrayList<Map<String, Object>> getWeightGoals() {
        return activeWeightGoals;
    }

    public void setWeightGoals(ArrayList<Map<String, Object>> weightGoals) {
        this.activeWeightGoals = weightGoals;
    }

    public ArrayList<Map<String, Object>> getExerciseGoals() {
        return activeExerciseGoals;
    }

    public void setExerciseGoals(ArrayList<Map<String, Object>> exerciseGoals) {
        this.activeExerciseGoals = exerciseGoals;
    }

    public ArrayList<Map<String, Object>> getWorkoutGoals() {
        return activeWorkoutGoals;
    }

    public void setWorkoutGoals(ArrayList<Map<String, Object>> workoutGoals) {
        this.activeWorkoutGoals = workoutGoals;
    }

    private ArrayList<Map<String, Object>> activeWorkoutGoals;
    private ArrayList<Map<String, Object>> nonActiveWorkoutGoals;
    private ArrayList<Map<String, Object>> completedWorkoutGoals;
    public ArrayList<Boolean> getGoalCollection() {
        return goalCollection;
    }

    public void setGoalCollection(ArrayList<Boolean> goalCollection) {
        this.goalCollection = goalCollection;
    }

    public Goal() {
        goalCollection = new ArrayList<>();
        activeStepGoals = new ArrayList<>();
        activeHydrationGoals = new ArrayList<>();
        activeWeightGoals = new ArrayList<>();
        activeExerciseGoals = new ArrayList<>();
        activeWorkoutGoals = new ArrayList<>();
        nonActiveStepGoals = new ArrayList<>();
        nonActiveHydrationGoals = new ArrayList<>();
        nonActiveWeightGoals = new ArrayList<>();
        nonActiveExerciseGoals = new ArrayList<>();
        nonActiveWorkoutGoals = new ArrayList<>();
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
        setActiveCollections(collectionPaths);
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
                        // Iterate through every document in the 'Exercise' collection
                        String title = document.getString("title");
                        Object completedField = document.get("completed");
                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        Object end_year = document.getLong("end_year");
                        Object end_month = document.getLong("end_month");
                        Object end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of((Integer) end_year, (Integer) end_month, (Integer) end_day);
                        if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                        if (title != null && title.equals("steps")) {
                            // 'title' field is equal to 'exercise' in this document
                            Map<String, Object> documentData = document.getData();
                            activeStepGoals.add(documentData);
                        }
                    }else {
                            if (title != null && title.equals("steps")) {
                                Map<String, Object> documentData = document.getData();
                                Map<String, Object> completedMap = (Map<String, Object>) completedField;
                                boolean flag = true; // Initialize the flag to true

                                for (Object value : completedMap.values()) {
                                    if (value instanceof Boolean) {
                                        if (!(Boolean) value) {
                                            flag = false; // If any boolean value is false, set the flag to false and break
                                            break;
                                        }
                                    }
                                }
                                if (flag == false)
                                    nonActiveWorkoutGoals.add(documentData);
                                else
                                    completedWorkoutGoals.add(documentData);
                            }
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
    public void queryHydrationGoals() {
        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
        goalCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String title = document.getString("title");
                        Object completedField = document.get("completed");
                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        Object end_year = document.getLong("end_year");
                        Object end_month = document.getLong("end_month");
                        Object end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of((Integer) end_year, (Integer) end_month, (Integer) end_day);
                        if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                            if (title != null && title.equals("hydration")) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeHydrationGoals.add(documentData);
                            } else {
                                if (title != null && title.equals("hydration")) {
                                    Map<String, Object> documentData = document.getData();
                                    Map<String, Object> completedMap = (Map<String, Object>) completedField;
                                    boolean flag = true; // Initialize the flag to true

                                    for (Object value : completedMap.values()) {
                                        if (value instanceof Boolean) {
                                            if (!(Boolean) value) {
                                                flag = false; // If any boolean value is false, set the flag to false and break
                                                break;
                                            }
                                        }
                                    }
                                    if (flag == false)
                                        nonActiveWorkoutGoals.add(documentData);
                                    else
                                        completedWorkoutGoals.add(documentData);
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
                        String title = document.getString("title");
                        Object completedField = document.get("completed");
                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        Object end_year = document.getLong("end_year");
                        Object end_month = document.getLong("end_month");
                        Object end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of((Integer) end_year, (Integer) end_month, (Integer) end_day);
                        if((Boolean)completedField){
                            if (title != null && title.equals("weight")) {
                                Map<String, Object> documentData = document.getData();
                                completedWeightGoals.add(documentData);
                            }
                        }
                        else if ((currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) && !((Boolean)completedField)) {
                            if (title != null && title.equals("weight")) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeWeightGoals.add(documentData);
                            }
                        }
                        else if (currentDate.isAfter(endDate)){
                            if (title != null && title.equals("weight")) {
                                Map<String, Object> documentData = document.getData();
                                nonActiveWeightGoals.add(documentData);
                            }
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
                        Object completedField = document.get("completed");
                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        Object end_year = document.getLong("end_year");
                        Object end_month = document.getLong("end_month");
                        Object end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of((Integer) end_year, (Integer) end_month, (Integer) end_day);
                        if((Boolean)completedField){
                            if (title != null && title.equals("weight")) {
                                Map<String, Object> documentData = document.getData();
                                completedExerciseGoals.add(documentData);
                            }
                        }
                        else if ((currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) && !((Boolean)completedField)) {
                            if (title != null && title.equals("weight")) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeExerciseGoals.add(documentData);
                            }
                        }
                        else if (currentDate.isAfter(endDate)){
                            if (title != null && title.equals("weight")) {
                                Map<String, Object> documentData = document.getData();
                                nonActiveExerciseGoals.add(documentData);
                            }
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
                        Object completedField = document.get("completed");
                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        Object end_year = document.getLong("end_year");
                        Object end_month = document.getLong("end_month");
                        Object end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of((Integer) end_year, (Integer) end_month, (Integer) end_day);
                        if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                            if (title != null && title.equals("workout")) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeWorkoutGoals.add(documentData);
                            }
                        }else {
                            if (title != null && title.equals("workout")) {
                                Map<String, Object> documentData = document.getData();
                                Map<String, Object> completedMap = (Map<String, Object>) completedField;
                                boolean flag = true; // Initialize the flag to true

                                for (Object value : completedMap.values()) {
                                    if (value instanceof Boolean) {
                                        if (!(Boolean) value) {
                                            flag = false; // If any boolean value is false, set the flag to false and break
                                            break;
                                        }
                                    }
                                }
                                if (flag == false)
                                    nonActiveWorkoutGoals.add(documentData);
                                else
                                    completedWorkoutGoals.add(documentData);
                            }
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

    public void setActiveCollections(String[] paths) {
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