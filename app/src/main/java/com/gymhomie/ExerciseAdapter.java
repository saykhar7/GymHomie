package com.gymhomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.tools.Achievement;
import com.gymhomie.workouts.exercise;
import com.gymhomie.workouts.workout;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    private List<exercise> exercise;
    private Context context;
    public ExerciseAdapter(Context context, List<exercise> exercises) {
        this.context = context;
        this.exercise = exercises;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseAdapter.ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        exercise currentExercise = exercise.get(position);
        holder.bind(currentExercise);
    }

    @Override
    public int getItemCount() {
        return exercise.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseName;
        private TextView exerciseNumSets;
        private TextView exerciseWeight;
        private TextView exerciseNumReps;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseTitle);
            exerciseNumSets = itemView.findViewById(R.id.setsText);
            exerciseWeight = itemView.findViewById(R.id.weightNumber);
            exerciseNumReps = itemView.findViewById(R.id.repsText);
        }
        public void bind(exercise exercise) {
            // bind achievement data to UI
            exerciseName.setText(exercise.getExerciseName());
            exerciseNumSets.setText(String.valueOf(exercise.getNumSets()));
            exerciseWeight.setText(exercise.getWeight());
            exerciseNumReps.setText(String.valueOf(exercise.getNumReps()));
        }
    }
}
