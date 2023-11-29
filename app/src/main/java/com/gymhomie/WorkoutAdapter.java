package com.gymhomie;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private List<exercise> exercises;
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

        boolean isExpandable = currentWorkout.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWorkout.setExpandable(!currentWorkout.isExpandable());
                exercises = currentWorkout.getExercises();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        // Set the adapter and layout manager only if it's expandable
        if (isExpandable) {
            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(context, currentWorkout.getExercises());
            holder.exerciseRecycler.setLayoutManager(new LinearLayoutManager(context));

            // Remove the following line to fix lint error
            // holder.exerciseRecycler.setHasFixedSize(true); // Set it only when needed

            holder.exerciseRecycler.setAdapter(exerciseAdapter);
        } else {
            holder.exerciseRecycler.setAdapter(null); // Clear adapter if not expandable
        }
    }



    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView workoutName;
        private TextView muscleGroups;
        private RecyclerView exerciseRecycler;
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            exerciseRecycler = itemView.findViewById(R.id.exerciseRecycler);
            workoutName = itemView.findViewById(R.id.workoutTitle);
            muscleGroups = itemView.findViewById(R.id.muscleGroupsTitle);
        }
        public void bind(workout workout) {
            // bind achievement data to UI
            workoutName.setText(workout.getName());
            muscleGroups.setText(workout.getMuscleGroups().toString());
        }
    }
}
