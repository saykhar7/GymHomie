package com.gymhomie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.tools.GymReminder;

import org.w3c.dom.Text;

import java.util.List;

public class GymReminderAdapter extends RecyclerView.Adapter<GymReminderAdapter.GymReminderViewHolder> {
    private final List<GymReminder> gymReminders;
    private final Context context;
    private OnCancelClickListener onCancelClickListener;

    public GymReminderAdapter(Context context, List<GymReminder> gymReminders) {
        this.context = context;
        this.gymReminders = gymReminders;
    }

    @NonNull
    @Override
    public GymReminderAdapter.GymReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_reminder_item, parent, false);
        return new GymReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymReminderAdapter.GymReminderViewHolder holder, int position) {
        GymReminder currentReminder = gymReminders.get(position);
        holder.bind(currentReminder);
    }

    @Override
    public int getItemCount() {
        return gymReminders.size();
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.onCancelClickListener = listener;
    }
    public class GymReminderViewHolder extends RecyclerView.ViewHolder {
        private final TextView reminderTitle;
        private final TextView reminderDay;
        private final TextView reminderTime;
        public GymReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderTitle = itemView.findViewById(R.id.gym_reminder_title);
            reminderDay = itemView.findViewById(R.id.gym_reminder_day);
            reminderTime = itemView.findViewById(R.id.gym_reminder_time);
            // initialize UI elements
            Button cancelButton = itemView.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCancelClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Log.d("GymReminderAdapter", "onCancelClickListener");
                            onCancelClickListener.onCancelClick(position);
                        }
                    }
                }
            });
        }
        public void bind(GymReminder gymReminder) {
            // bind other gym reminder data to UI as needed
            reminderTitle.setText(gymReminder.getWorkoutType());
            reminderDay.setText(gymReminder.getDayOfWeek());
            int hour = gymReminder.getReminderTimeHour();
            int minute = gymReminder.getReminderTimeMinute();
            reminderTime.setText("Time: " + formatTime(hour, minute));
        }
        public String formatTime(int hour, int minute) {
            String amPm;
            int formattedHour = hour;

            // Determine AM or PM
            if (hour < 12) {
                amPm = "am";
            } else {
                amPm = "pm";
                if (hour > 12) {
                    formattedHour = hour - 12;
                }
            }

            // Format minute with leading zero if needed
            String formattedMinute = String.format("%02d", minute);

            // Create the formatted time string
            return formattedHour + ":" + formattedMinute + " " + amPm;
        }
    }
    public interface OnCancelClickListener {
        void onCancelClick(int position);
    }
}
