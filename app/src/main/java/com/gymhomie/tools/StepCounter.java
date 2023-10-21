package com.gymhomie.tools;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

public class StepCounter {
    private int day;
    private int month;
    private int year;
    private int steps;
    private final double averageStrideLength = 2.5; // We can calculate this based on height and other measurables later

    public StepCounter() {}

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
    public ArrayList<ArrayList<String>> retrieveStepData() {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        firestore.collection("users")
                .document(auth.getUid())
                .collection("StepCounter").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            StepCounter util = new StepCounter(document.getLong("day"), document.getLong("month"), document.getLong("year"), document.getLong("steps"));
                            ArrayList<String> items = new ArrayList<>();
                            items.add(util.getDateString());
                            items.add(String.valueOf(util.feetTravelled()));
                            items.add(String.valueOf(util.milesTravelled()));
                            list.add(items);
                        }
                    }
                });
        return list;

    }
    public ArrayList<Float> getDates(ArrayList<ArrayList<String>> list){
        ArrayList<Float> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            res.add(Float.parseFloat(list.get(i).get(0)));
        }
        return res;
    }
    public ArrayList<Float> getFeetMetrics(ArrayList<ArrayList<String>> list){
        ArrayList<Float> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            res.add(Float.parseFloat(list.get(i).get(0)));
        }
        return res;
    }
    public ArrayList<Float> getMilesMetrics(ArrayList<ArrayList<String>> list){
        ArrayList<Float> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            res.add(Float.parseFloat(list.get(i).get(0)));
        }
        return res;
    }
    public String getDateString(){
        // This is for UI display, so we want M/DD
        String res = extractMonth(this.month) + "." + extractDay(this.day);
        return res;
    }
    public String getDateString(int month, int day){
        // This is for UI display, so we want M/DD
        String res = extractMonth(month) + "." + extractDay(day);
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
