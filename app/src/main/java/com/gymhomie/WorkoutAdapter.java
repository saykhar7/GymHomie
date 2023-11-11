package com.gymhomie;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.tools.Achievement;
import com.gymhomie.workouts.exercise;
import com.gymhomie.workouts.workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>{

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<workout> workouts;
    private Context context;
    public WorkoutAdapter(Context context, List<workout> workouts) {
        this.context = context;
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutViewHolder holder, int position) {
        workout currentWorkout = workouts.get(position);
        holder.bind(currentWorkout);
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(context, currentWorkout.getExercises());
        holder.exerciseRecycler.setAdapter(exerciseAdapter);
        holder.exerciseRecycler.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView workoutName;
        private TextView muscleGroups;
        private RecyclerView exerciseRecycler;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseRecycler = itemView.findViewById(R.id.exerciseRecycler);
            workoutName = itemView.findViewById(R.id.workoutTitle);
            muscleGroups = itemView.findViewById(R.id.muscleGroupsTitle);
            //exerciseList = itemView.findViewById(R.id.exercises_text_view_placeholder);
        }
        public void bind(workout workout) {
            // bind achievement data to UI
            workoutName.setText(workout.getName());
            muscleGroups.setText(workout.getMuscleGroups().toString());
        }
    }
}
