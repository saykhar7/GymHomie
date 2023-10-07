package com.gymhomie.tools;

public class GymReminder {
    private String workoutType; // TODO: change to use the GymHomie Workout type
    private String dayOfWeek; // Mon, Tue, etc
    private int reminderTimeHour; // TODO: must find best time type for firestore and android notifications
    private int reminderTimeMinute;
    private int notificationID; //unique id (atomic) so we can delete repeating alarms by referencing db
    public GymReminder () {
        // Default and empty constructor required for Firestore
    }

    // Constructor with parameters
    public GymReminder (String workoutType, String dayOfWeek, int reminderTimeHour, int reminderTimeMinute, int notificationID) {
        this.workoutType = workoutType;
        this.dayOfWeek = dayOfWeek;
        this.reminderTimeHour = reminderTimeHour;
        this.reminderTimeMinute = reminderTimeMinute;
        this.notificationID = notificationID;
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

    public int getNotificationID() { return notificationID; }

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

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }
}
