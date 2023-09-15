package com.gymhomie.tools;

public class StepCounter {
    private int day;
    private int month;
    private int year;
    private int steps;
    private final double averageStrideLength = 2.5;

    StepCounter(int day, int month, int year, int steps){
        this.steps = steps;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    StepCounter(int day, int month, int year){
        this.year = year;
        this.month = month;
        this.day = day;
    }
    StepCounter(int steps){
        this.steps = steps;
    }

    public StepCounter(String day, String month, String year, String steps){
        this.steps = Integer.parseInt(steps);
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        this.day = Integer.parseInt(day);
    }
    StepCounter(String day, String month, String year){
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        this.day = Integer.parseInt(day);
    }
    StepCounter(String steps){
        this.steps = Integer.parseInt(steps);
    }

    public StepCounter(Long day, Long month, Long year, Long steps) {
        this.steps = Math.toIntExact(steps);
        this.year = Math.toIntExact(year);
        this.month = Math.toIntExact(month);
        this.day = Math.toIntExact(day);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
    public String getDateString(){
        // This is for UI display, so we want M/DD
        String res = extractMonth(this.month) + "/" + extractDay(this.day);
        return res;
    }
    public String getDateString(int month, int day){
        // This is for UI display, so we want M/DD
        String res = extractMonth(month) + "/" + extractDay(day);
        return res;
    }
    public String extractDay(){
        String res = "";
        Integer wrap = new Integer(this.day);
        res = wrap.toString();
        return res;
    }
    public String extractMonth(){
        String res = "";
        Integer wrap = new Integer(this.month);
        res = wrap.toString();
        return res;
    }
    public String extractDay(int day){
        String res = "";
        Integer wrap = new Integer(day);
        res = wrap.toString();
        return res;
    }
    public String extractMonth(int month){
        String res = "";
        Integer wrap = new Integer(month);
        res = wrap.toString();
        return res;
    }
    public String extractYear(int year){
        String res = "";
        Integer wrap = new Integer(year);
        // Get last 2 from year
        res = wrap.toString().substring(2,3);
        return res;
    }
    public double feetTravelled(){
        double res = 0;
        res = this.steps * averageStrideLength;
        return res;
    }
    public double milesTravelled(){
        double res = 0;
        res = (this.steps * averageStrideLength) / 5280;
        return res;
    }
    public double feetTravelled(int steps){
        double res = 0;
        res = steps * averageStrideLength;
        return res;
    }
    public double milesTravelled(int steps){
        double res = 0;
        res = (steps * averageStrideLength) / 5280;
        return res;
    }
}
