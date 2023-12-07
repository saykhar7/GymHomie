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
                exerciseSeconds, exerciseMinutes, setsText, repsText, minText, secsText;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseTitle);
            exerciseNumSets = itemView.findViewById(R.id.numberSets);
            exerciseWeight = itemView.findViewById(R.id.weightNumber);
            exerciseNumReps = itemView.findViewById(R.id.numberReps);
            exerciseSeconds = itemView.findViewById(R.id.num_seconds);
            exerciseMinutes = itemView.findViewById(R.id.num_minutes);
            //setsText = itemView.findViewById(R.id.setsText);
            repsText = itemView.findViewById(R.id.repsText);
            minText = itemView.findViewById(R.id.minutesText);
            secsText = itemView.findViewById(R.id.secondsText);
        }
        public void bind(exercise exercise) {
            // bind achievement data to UI
            exerciseName.setText(exercise.getExerciseName().toString());
            exerciseWeight.setText(String.valueOf(exercise.getWeight()));
            exerciseNumSets.setText(String.valueOf(exercise.getNumSets()));
            if(!exercise.isTimed())
            {
                exerciseMinutes.setVisibility(View.GONE);
                exerciseSeconds.setVisibility(View.GONE);
                minText.setVisibility(View.GONE);
                secsText.setVisibility(View.GONE);
                exerciseNumReps.setVisibility(View.VISIBLE);
                repsText.setVisibility(View.VISIBLE);
                exerciseNumReps.setText(String.valueOf(exercise.getNumReps()));
            }
            else {
                exerciseNumReps.setVisibility(View.GONE);
                repsText.setVisibility(View.GONE);
                exerciseMinutes.setVisibility(View.VISIBLE);
                exerciseSeconds.setVisibility(View.VISIBLE);
                minText.setVisibility(View.VISIBLE);
                secsText.setVisibility(View.VISIBLE);
                exerciseMinutes.setText((String.valueOf(exercise.getMinutes())));
                exerciseSeconds.setText(String.valueOf(exercise.getSeconds()));
            }

        }
    }
}
