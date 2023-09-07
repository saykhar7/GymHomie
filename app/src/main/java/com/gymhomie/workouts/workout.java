package com.gymhomie.workouts;

import java.util.List;

public class workout {

    String name;
    List<String> muscleGroups;
    List<exercise> exercises;

    public workout(String name, List<String> muscleGroups, List<exercise> exercises)
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
    public void setMuscleGroups(List<String> muscleGroups) {this.muscleGroups = muscleGroups;}

    //sets list of exercises to the workout
    public void setExercise(List<exercise> exercises) {this.exercises = exercises;}

    //adds exercise to exercise list
    public void addExercise(exercise exercise) {exercises.add(exercise);}


}

