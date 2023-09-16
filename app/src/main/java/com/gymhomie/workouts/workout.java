package com.gymhomie.workouts;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.gymhomie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class workout {

    static String name;
    ArrayList<String> muscleGroups;
    ArrayList<exercise> exercises;

    public workout(String name, ArrayList<String> muscleGroups, ArrayList<exercise> exercises)
    {
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.exercises = exercises;
    }

    //gets name of the workout
    public String getName() {return name;}

    //gets list of muscle groups in one workout
    public List<String> getMuscleGroups() {return muscleGroups;}

    //gets list of exercises in one workout
    public List<exercise> getExercises() {return exercises;}


    //sets name of the workout
    public void setName(String name) {this.name = name;}

    //sets list of muscle groups to the workout
    public void setMuscleGroups(ArrayList<String> muscleGroups) {this.muscleGroups = muscleGroups;}

    //sets list of exercises to the workout
    public void setExercise(ArrayList<exercise> exercises) {this.exercises = exercises;}

    //adds exercise to exercise list
    public void addexercise()
    {
        new addExercise();
    }

    static class exercise{
        String workoutName;
        MultiAutoCompleteTextView exerciseName;
        NumberPicker numSets;
        int minutes;
        int seconds;
        NumberPicker numReps;
        NumberPicker weight;


        public exercise(MultiAutoCompleteTextView exerciseName, NumberPicker numSets, NumberPicker numReps, NumberPicker weight)
        {
            this.exerciseName = exerciseName;
            this.numSets = numSets;
            this.numReps = numReps;
            this.weight = weight;
        }

        public exercise(MultiAutoCompleteTextView exerciseName, NumberPicker numSets, int minutes, int seconds, NumberPicker weight)
        {
            this.exerciseName = exerciseName;
            this.numSets = numSets;
            this.minutes = minutes;
            this.seconds = seconds;
            this.weight = weight;
        }

        public String getWorkoutName() {return name;}
        //gets name of the exercise
        public MultiAutoCompleteTextView getName() {return exerciseName;}

        //gets number of sets for the exercise
        public NumberPicker getNumSets() {return numSets;}

        //gets amount of time for the exercise
        public int getTime() {return minutes;}

        public int getSeconds() {return this.seconds;}

        //gets number of reps in one set
        public NumberPicker getReps() {return numReps;}

        //gets amount of weight for each set
        public NumberPicker getWeight() {return weight;}



        public void setWorkoutName() {this.workoutName = name;}
        //sets name of the exercise
        public void setName() {this.exerciseName = exerciseName;}

        //sets number of sets for the exercise
        public void setNumSets() {this.numSets = numSets;}

        //sets amount of time for the exercise
        public void setMinutes() {this.minutes = minutes;}

        public void setSeconds() {this.seconds = seconds;}

        //sets number of reps in one set
        public void setNumReps() {this.numReps = numReps;}

        //sets amount of weight for each set
        public void setWeight() {this.weight = weight;}

    }

}

