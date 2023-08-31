package com.gymhomie.tools;

public class WaterIntake {
    private String date;
    private String time;
    private int amount; // in milliliters

    public WaterIntake() {
        // Default and empty constructor required for Firestore
    }

    // Constructor with parameters
    public WaterIntake(String date, String time, int amount) {
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }
}
