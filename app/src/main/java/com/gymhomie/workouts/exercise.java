package com.gymhomie.workouts;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.gymhomie.workouts.workout;

public class exercise implements Parcelable {
    private String exerciseName;
    private int minutes = 0;
    private int seconds = 0;
    private int weight = 0;
    private int numReps = 0;
    private int numSets = 0;
    private boolean isTimed;

    public exercise() {
    }

    protected exercise(Parcel in) {
        exerciseName = in.readString();
        minutes = in.readInt();
        seconds = in.readInt();
        weight = in.readInt();
        numReps = in.readInt();
        numSets = in.readInt();
        isTimed = in.readInt() != 0;
    }

    public static final Creator<exercise> CREATOR = new Creator<exercise>() {
        @Override
        public exercise createFromParcel(Parcel in) {
            return new exercise(in);
        }

        @Override
        public exercise[] newArray(int size) {
            return new exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(exerciseName);
        parcel.writeInt(minutes);
        parcel.writeInt(seconds);
        parcel.writeInt(weight);
        parcel.writeInt(numReps);
        parcel.writeInt(numSets);
        parcel.writeInt(isTimed ? 1 : 0);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;

    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }
    public int getNumSets() {
        return numSets;
    }

    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }

    public boolean isTimed() {
        return isTimed;
    }

    public void setTimed(boolean timed) {
        isTimed = timed;
    }
}

