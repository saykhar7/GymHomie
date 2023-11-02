package com.gymhomie.tools;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private String user_id;
    private ArrayList<Boolean> goalCollection;
    private ArrayList<Boolean> completedGoalCollection;
    private int current_steps;
    private int current_ounces;
    private int current_weight;
    private int current_max;
    private int current_streak;
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
    private ArrayList<Map<String, Object>> activeWorkoutGoals;
    private ArrayList<Map<String, Object>> nonActiveWorkoutGoals;
    private ArrayList<Map<String, Object>> completedWorkoutGoals;

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
    public ArrayList<Boolean> getGoalCollection() {
        return goalCollection;
    }

    public void setCompletedGoalCollection(ArrayList<Boolean> completedGoalCollection) {
        this.completedGoalCollection = completedGoalCollection;
    }

    public int getCurrent_steps() {
        return current_steps;
    }
    public void getTodaysHydrationData(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LocalDate currentDate = LocalDate.now();

        firestore.collection("users")
                .document(auth.getUid())
                .collection("WaterIntakes")
                .whereEqualTo("day", currentDate.getDayOfMonth())
                .whereEqualTo("month", currentDate.getMonthValue())
                .whereEqualTo("year", currentDate.getYear())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // get the number of steps for the day
                            Long amount = document.getLong("amount");
                            if (amount != null) {
                                current_ounces += amount.intValue();
                            }
                        }
                    }
                });
    }
    public void getCurrentWeight(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LocalDate currentDate = LocalDate.now();

        firestore.collection("users")
                .document(auth.getUid())
                .collection("Profile")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // get the number of steps for the day
                            Long weight = document.getLong("weight");
                            if (weight != null) {
                                current_weight = weight.intValue();
                            }
                        }
                    }
                });
    }

    public void getTodaysStepData(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LocalDate currentDate = LocalDate.now();

        firestore.collection("users")
                .document(auth.getUid())
                .collection("StepCounter")
                .whereEqualTo("day", currentDate.getDayOfMonth())
                .whereEqualTo("month", currentDate.getMonthValue())
                .whereEqualTo("year", currentDate.getYear())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // get the number of steps for the day
                            Long stepsValue = document.getLong("steps");
                            if (stepsValue != null) {
                                current_steps += stepsValue.intValue();
                            }
                        }
                    }
                });
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

    public void setGoalCollection(ArrayList<Boolean> goalCollection) {
        this.goalCollection = goalCollection;
    }

    public void setCurrent_steps(int current_steps) {
        this.current_steps = current_steps;
    }

    public int getCurrent_ounces() {
        return current_ounces;
    }

    public void setCurrent_ounces(int current_ounces) {
        this.current_ounces = current_ounces;
    }

    public int getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(int current_weight) {
        this.current_weight = current_weight;
    }

    public int getCurrent_max() {
        return current_max;
    }

    public void setCurrent_max(int current_max) {
        this.current_max = current_max;
    }

    public int getCurrent_streak() {
        return current_streak;
    }

    public void setCurrent_streak(int current_streak) {
        this.current_streak = current_streak;
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
        completedStepGoals = new ArrayList<>();
        completedHydrationGoals = new ArrayList<>();
        completedWeightGoals = new ArrayList<>();
        completedExerciseGoals = new ArrayList<>();
        completedWorkoutGoals = new ArrayList<>();
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
        getTodaysStepData();
        getTodaysHydrationData();
        getCurrentWeight();
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

    public void queryStepCounterGoals() {
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

                        Long end_year = document.getLong("end_year");
                        Long end_month = document.getLong("end_month");
                        Long end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of(end_year.intValue(), end_month.intValue(), end_day.intValue());
                        if (title != null && title.equals("steps")) {
                        if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                                Map<String, Object> documentData = document.getData();
                                activeStepGoals.add(documentData);
                        } else {
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
                                if (!flag)
                                    nonActiveStepGoals.add(documentData);
                                else
                                    completedStepGoals.add(documentData);
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

                        Long end_year = document.getLong("end_year");
                        Long end_month = document.getLong("end_month");
                        Long end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of(end_year.intValue(), end_month.intValue(), end_day.intValue());
                        if (title != null && title.equals("hydration")) {
                        if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeHydrationGoals.add(documentData);
                            } else {
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
                                    if (!flag)
                                        nonActiveHydrationGoals.add(documentData);
                                    else
                                        completedHydrationGoals.add(documentData);
                                }
                            }
                    }
                }else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }

    public void queryWeightGoals() {
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

                        Long end_year = document.getLong("end_year");
                        Long end_month = document.getLong("end_month");
                        Long end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of(end_year.intValue(), end_month.intValue(), end_day.intValue());
                        if (title != null && title.equals("weight")) {
                            if ((Boolean) completedField) {
                                Map<String, Object> documentData = document.getData();
                                completedWeightGoals.add(documentData);
                            } else if ((currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) && !((Boolean) completedField)) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeWeightGoals.add(documentData);
                            } else if (currentDate.isAfter(endDate)) {
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

    public void queryExerciseGoals() {
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

                        Long end_year = document.getLong("end_year");
                        Long end_month = document.getLong("end_month");
                        Long end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of(end_year.intValue(), end_month.intValue(), end_day.intValue());
                        if (title != null && title.equals("exercise")) {
                            if ((Boolean) completedField) {
                                Map<String, Object> documentData = document.getData();
                                completedExerciseGoals.add(documentData);
                            } else if ((currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) && !((Boolean) completedField)) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeExerciseGoals.add(documentData);
                            } else if (currentDate.isAfter(endDate)) {
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

    public void queryWorkoutGoals() {
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

                        Long end_year = document.getLong("end_year");
                        Long end_month = document.getLong("end_month");
                        Long end_day = document.getLong("end_day");

                        LocalDate endDate = LocalDate.of(end_year.intValue(), end_month.intValue(), end_day.intValue());
                        if (title != null && title.equals("workout")) {
                            if (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
                                // 'title' field is equal to 'exercise' in this document
                                Map<String, Object> documentData = document.getData();
                                activeWorkoutGoals.add(documentData);
                            } else {
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
                                if (!flag)
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