package com.gymhomie.tools;

public class GymReminder {
    private String workoutType; // TODO: change to use the GymHomie Workout type
    private String dayOfWeek; // Mon, Tue, etc
    private int reminderTimeHour; // TODO: must find best time type for firestore and android notifications
    private int reminderTimeMinute;

    public GymReminder () {
        // Default and empty constructor required for Firestore
    }

    // Constructor with parameters
    public GymReminder (String workoutType, String dayOfWeek, int reminderTimeHour, int reminderTimeMinute) {
        this.workoutType = workoutType;
        this.dayOfWeek = dayOfWeek;
        this.reminderTimeHour = reminderTimeHour;
        this.reminderTimeMinute = reminderTimeMinute;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getReminderTimeHour() {
        return reminderTimeHour;
    }
    public int getReminderTimeMinute() { return reminderTimeMinute; }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setReminderTimeHour(int reminderTimeHour) {
        this.reminderTimeHour = reminderTimeHour;
    }
    public void setReminderTimeMinute(int reminderTimeMinute) {
        this.reminderTimeMinute = reminderTimeMinute;
    }
}
