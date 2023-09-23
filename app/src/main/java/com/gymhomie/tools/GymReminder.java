package com.gymhomie.tools;

public class GymReminder {
    private String workoutType; // TODO: change to use the GymHomie Workout type
    private String dayOfWeek; // Mon, Tue, etc
    private String reminderTime; // TODO: must find best time type for firestore and android notifications

    public GymReminder () {
        // Default and empty constructor required for Firestore
    }

    // Constructor with parameters
    public GymReminder (String workoutType, String dayOfWeek, String reminderTime) {
        this.workoutType = workoutType;
        this.dayOfWeek = dayOfWeek;
        this.reminderTime = reminderTime;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}
