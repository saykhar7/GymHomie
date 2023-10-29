package com.gymhomie.workouts;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

public class workout implements Parcelable {

    static String name;
    private ArrayList<String> muscleGroups = new ArrayList<String>();
    private ArrayList<exercise> exercises = new ArrayList<exercise>();

    public workout(){};
    public workout(String name, ArrayList<String> muscleGroups, ArrayList<exercise> exercises)
    {
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.exercises = exercises;
    }


    protected workout(Parcel in) {
        muscleGroups = in.createStringArrayList();
    }

    public static final Creator<workout> CREATOR = new Creator<workout>() {
        @Override
        public workout createFromParcel(Parcel in) {
            return new workout(in);
        }

        @Override
        public workout[] newArray(int size) {
            return new workout[size];
        }
    };

    //gets name of the workout
    public String getName() {return name;}

    //gets list of muscle groups in one workout
    public ArrayList<String> getMuscleGroups() {return muscleGroups;}

    //gets list of exercises in one workout
    public ArrayList<exercise> getExercises() {return exercises;}


    //sets name of the workout
    public void setName(String name) {this.name = name;}

    //sets list of muscle groups to the workout
    public void setMuscleGroups(ArrayList<String> muscleGroups) {this.muscleGroups = muscleGroups;}

    //sets list of exercises to the workout
    public void setExercise(ArrayList<exercise> exercises) {this.exercises = exercises;}

    //adds exercise to exercise list
    public void addExercise(exercise e)
    {
        exercises.add(e);
    }

    public void removeExercise(exercise e)
    {
        exercises.remove(e);
    }

    //adds muscle group
    public void addMG(String muscle)
    {
        muscleGroups.add(muscle);
    }

    //removes muscle group
    public void removeMG(String muscle)
    {
        muscleGroups.remove(muscle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringList(muscleGroups);
    }

    public static class exercise{
        MultiAutoCompleteTextView exerciseName;
        NumberPicker numSets;
        int minutes;
        EditText seconds;
        NumberPicker numReps;
        EditText weight;


        public exercise(MultiAutoCompleteTextView exerciseName, NumberPicker numSets, NumberPicker numReps, EditText weight)
        {
            this.exerciseName = exerciseName;
            this.numSets = numSets;
            this.numReps = numReps;
            this.weight = weight;
        }

        public exercise(MultiAutoCompleteTextView exerciseName, NumberPicker numSets, int minutes, EditText seconds, EditText weight)
        {
            this.exerciseName = exerciseName;
            this.numSets = numSets;
            this.minutes = minutes;
            this.seconds = seconds;
            this.weight = weight;
        }

        public exercise() {

        }

        public String getWorkoutName() {return name;}
        //gets name of the exercise
        public MultiAutoCompleteTextView getName() {return exerciseName;}

        //gets number of sets for the exercise
        public NumberPicker getNumSets() {return numSets;}

        //gets amount of time for the exercise
        public int getTime() {return minutes;}

        public EditText getSeconds() {return this.seconds;}

        //gets number of reps in one set
        public NumberPicker getReps() {return numReps;}

        //gets amount of weight for each set
        public EditText getWeight() {return weight;}



        //sets name of the exercise
        public void setName(MultiAutoCompleteTextView name) {this.exerciseName = name;}

        //sets number of sets for the exercise
        public void setNumSets(NumberPicker sets) {this.numSets = sets;}

        //sets amount of time for the exercise
        public void setMinutes(int min) {this.minutes = min;}

        public void setSeconds(EditText sec) {this.seconds = sec;}

        //sets number of reps in one set
        public void setNumReps(NumberPicker reps) {this.numReps = reps;}

        //sets amount of weight for each set
        public void setWeight(EditText weight) {this.weight = weight;}

    }

}

