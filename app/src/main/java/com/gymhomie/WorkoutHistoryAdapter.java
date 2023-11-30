package com.gymhomie;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder> {
    private ArrayList<Map<String, Object>> workouts;

    public WorkoutHistoryAdapter(ArrayList<Map<String, Object>> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> workout = workouts.get(position);
        holder.bind(workout);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your TextViews here
        TextView nameTextView;
        TextView muscleGroupsTextView;
        TextView publicIdentifier;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your TextViews
            nameTextView = itemView.findViewById(R.id.nameTextView);
            muscleGroupsTextView = itemView.findViewById(R.id.muscleGroupsTextView);
            publicIdentifier = itemView.findViewById(R.id.public_identifier);
        }
        public void bind(Map<String, Object> workout) {
            // Check if the workout map is null
            if (workout == null) {
                return;
            }

            // Bind data to your TextViews
            Object nameObject = workout.get("name");
            nameTextView.setText("Workout Name: " + (nameObject != null ? nameObject.toString() : ""));

            Object muscleGroupsObject = workout.get("muscleGroups");
            if (muscleGroupsObject instanceof List) {
                List<String> muscleGroups = (List<String>) muscleGroupsObject;
                muscleGroupsTextView.setText("Muscle Groups: " + TextUtils.join(", ", muscleGroups));
            } else {
                muscleGroupsTextView.setText("Muscle Groups: N/A");
            }

            Object publicObject = workout.get("public");
            publicIdentifier.setText("Public: " + (publicObject != null ? publicObject.toString() : "N/A"));
        }

    }
}
