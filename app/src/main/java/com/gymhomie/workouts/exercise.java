package com.gymhomie.workouts;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.gymhomie.workouts.workout;

public class exercise implements Parcelable {
    private String exerciseName;
    private String minutes;
    private String seconds;
    private String weight;
    private int numReps;
    private int numSets;

    public exercise() {

    }

    protected exercise(Parcel in) {
        exerciseName = in.readString();
        minutes = in.readString();
        seconds = in.readString();
        weight = in.readString();
        numReps = in.readInt();
        numSets = in.readInt();
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
        parcel.writeString(minutes);
        parcel.writeString(seconds);
        parcel.writeString(weight);
        parcel.writeInt(numReps);
        parcel.writeInt(numSets);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
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
}