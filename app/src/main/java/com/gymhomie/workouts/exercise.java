package com.gymhomie.workouts;

import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;

public class exercise{

    MultiAutoCompleteTextView name;
    NumberPicker numSets;
    int minutes;
    int seconds;
    NumberPicker numReps;
    NumberPicker weight;


    public exercise(MultiAutoCompleteTextView name, NumberPicker numSets, NumberPicker numReps, NumberPicker weight)
    {
        this.name = name;
        this.numSets = numSets;
        this.numReps = numReps;
        this.weight = weight;
    }

    public exercise(MultiAutoCompleteTextView name, NumberPicker numSets, int minutes, int seconds, NumberPicker weight)
    {
        this.name = name;
        this.numSets = numSets;
        this.minutes = minutes;
        this.seconds = seconds;
        this.weight = weight;
    }

    //gets name of the exercise
    public MultiAutoCompleteTextView getName() {return name;}

    //gets number of sets for the exercise
    public NumberPicker getNumSets() {return numSets;}

    //gets amount of time for the exercise
    public int getTime() {return minutes;}

    public int getSeconds() {return this.seconds;}

    //gets number of reps in one set
    public NumberPicker getReps() {return numReps;}

    //gets amount of weight for each set
    public NumberPicker getWeight() {return weight;}



    //sets name of the exercise
    public void setName() {this.name = name;}

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
