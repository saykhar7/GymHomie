package com.gymhomie.workouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.R;

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
        private TextView exerciseName, exerciseNumSets, exerciseWeight, exerciseNumReps,
                exerciseSeconds, exerciseMinutes;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseTitle);
            exerciseNumSets = itemView.findViewById(R.id.numberSets);
            exerciseWeight = itemView.findViewById(R.id.weightNumber);
            exerciseNumReps = itemView.findViewById(R.id.numberReps);
            exerciseSeconds = itemView.findViewById(R.id.num_seconds);
            exerciseMinutes = itemView.findViewById(R.id.num_minutes);
        }
        public void bind(exercise exercise) {
            // bind achievement data to UI
            exerciseName.setText(exercise.getExerciseName().toString());
            exerciseWeight.setText(String.valueOf(exercise.getWeight()));
            exerciseNumSets.setText(String.valueOf(exercise.getNumSets()));
            exerciseNumReps.setText(String.valueOf(exercise.getNumReps()));
            exerciseMinutes.setText((String.valueOf(exercise.getMinutes())));
            exerciseSeconds.setText(String.valueOf(exercise.getSeconds()));

        }
    }
}
