package com.gymhomie.workouts;

import java.util.ArrayList;

public class AddWorkout {
    public ArrayList<String> muscleGroup;
    public workout myWorkout;
    public String name;
    public ArrayList<workout.exercise> exercises;

    public AddWorkout(){
        muscleGroup = new ArrayList<String>();
        exercises = new ArrayList<workout.exercise>();
        myWorkout = new workout(name, muscleGroup, exercises);
    }
}