package com.gymhomie.workouts;

//import static androidx.core.os.ParcelCompat.TiramisuImpl.readArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Date;

public class workout implements Parcelable {

    public static final String WORKOUT_PARCEL = "myWorkout";
    private String name;
    private ArrayList<String> muscleGroups = new ArrayList<String>();
    private ArrayList<exercise> exercises = new ArrayList<exercise>();
    private boolean isExpandable;

    public workout(){};
    public workout(String name, ArrayList<String> muscleGroups, ArrayList<exercise> exercises)
    {
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.exercises = exercises;
        this.isExpandable = false;
    }
    protected workout(Parcel in) {
        name = in.readString();
        muscleGroups = in.createStringArrayList();
        exercises = in.createTypedArrayList(exercise.CREATOR);
        isExpandable = in.readByte() != 0;
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

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

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
        parcel.writeString(name);
        parcel.writeStringList(muscleGroups);
        parcel.writeTypedList(exercises);
        parcel.writeByte((byte) (isExpandable ? 1 : 0));
    }


    class completed_workout {
        private workout w;
        private Date d;

        public completed_workout(workout w, Date d) {
            this.w = w;
            this.d = d;
        }

        public workout getW() {
            return w;
        }

        public Date getD() {
            return d;
        }

        public void setW(workout w) {
            this.w = w;
        }

        public void setD(Date d) {
            this.d = d;
        }
    }

}

