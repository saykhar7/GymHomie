package com.gymhomie;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HomieProfileViewAdapter extends RecyclerView.Adapter<HomieProfileViewAdapter.ViewHolder> {
    private List<Map<String, Object>> workouts;
    // Define a listener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HomieProfileViewAdapter(List<Map<String, Object>> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Map<String, Object> workout = workouts.get(position);
        holder.bind(workout);
        // Set an OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the listener is set
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your TextViews here
        TextView nameTextView;
        TextView muscleGroupsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your TextViews
            nameTextView = itemView.findViewById(R.id.nameTextView);
            muscleGroupsTextView = itemView.findViewById(R.id.muscleGroupsTextView);
        }

        public void bind(Map<String, Object> workout) {
            // Bind data to your TextViews
            nameTextView.setText("Workout Name: " + workout.get("name"));

            List<String> muscleGroups = (List<String>) workout.get("muscleGroups");
            muscleGroupsTextView.setText("Muscle Groups: " + TextUtils.join(", ", muscleGroups));
        }
    }
}
