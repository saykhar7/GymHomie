package com.gymhomie.workouts;
import androidx.lifecycle.ViewModel;

public class workoutViewModel extends ViewModel {
    private workout myWorkout = new workout();

    public workout getWorkout()
    {
        return myWorkout;
    }


}
