package com.gymhomie.workouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.R;

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
        if(isExpandable)
        {
            holder.start.setVisibility(View.VISIBLE);
        }
        else{
            holder.start.setVisibility(View.GONE);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentWorkout.setExpandable(!currentWorkout.isExpandable());
                exercises = currentWorkout.getExercises();
                notifyItemChanged(holder.getAdapterPosition());


            }
        });

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Start_Workout_Activity.class);
                context.startActivity(intent);
            }
        });

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(context, currentWorkout.getExercises());
        holder.exerciseRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.exerciseRecycler.setHasFixedSize(true);
        holder.exerciseRecycler.setAdapter(exerciseAdapter);
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
        private Button start;
        private Activity activity;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            exerciseRecycler = itemView.findViewById(R.id.exerciseRecycler);
            workoutName = itemView.findViewById(R.id.workoutTitle);
            muscleGroups = itemView.findViewById(R.id.muscleGroupsTitle);
            start = itemView.findViewById(R.id.start);
            //this.activity = activity;
        }
        public void bind(workout workout) {
            // bind achievement data to UI
            workoutName.setText(workout.getName());
            muscleGroups.setText(workout.getMuscleGroups().toString());
        }
    }
}
