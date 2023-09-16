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

    EditText name = null;
    ArrayList<String> muscleGroups;
    ArrayList<exercise> exercises;

    public workout(EditText name, ArrayList<String> muscleGroups, ArrayList<exercise> exercises)
    {
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.exercises = exercises;
    }

    //gets name of the workout
    public EditText getName() {return name;}

    //gets list of muscle groups in one workout
    public List<String> getMuscleGroups() {return muscleGroups;}

    //gets list of exercises in one workout
    public List<exercise> getExercises() {return exercises;}


    //sets name of the workout
    public void setName(EditText name) {this.name = name;}

    //sets list of muscle groups to the workout
    public void setMuscleGroups(ArrayList<String> muscleGroups) {this.muscleGroups = muscleGroups;}

    //sets list of exercises to the workout
    public void setExercise(ArrayList<exercise> exercises) {this.exercises = exercises;}

    //adds exercise to exercise list
    //public void addExercise(exercise exercise) {exercises.add(exercise);}

}

