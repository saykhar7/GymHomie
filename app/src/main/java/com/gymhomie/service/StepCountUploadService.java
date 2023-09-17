package com.gymhomie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StepCountUploadService extends Service {

    private static final String PREF_NAME = "StepCounterPrefs";
    private static final String LAST_RESET_KEY = "lastResetTime";
    private static final String STEP_COUNT_KEY = "stepCount";

    private String email;

    public FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if midnightTime is greater than lastResetTime
        long midnightTime = getMidnightTime(new Date());
        long lastResetTime = getLastResetTimeFromSharedPreferences(); // Implement this method

        if (midnightTime > lastResetTime) {
            // Send the stepCount and date to Firestore
            int stepCount = getStepCountFromSharedPreferences(); // Implement this method
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            // Extract the month, day, and year as numbers
            // Use int instead of string to save space in DB
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based, so add 1
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            Map<String, Object> data = new HashMap<>();
            data.put("steps", stepCount);
            data.put("day", day);
            data.put("month", month);
            data.put("year", year);

            // Initiate Firestore instance and authorization
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            // This new ID's purpose is so a new document will be created
            // for each entry into the StepCounter class
            String newDocumentId = firestore.collection("users")
                    .document(auth.getUid())
                    .collection("StepCounter")
                    .document().getId();

            // This is a block for potential email service. Where user's email is taken
            // and we can send daily, weekly, monthly, yearly, or customized reports.
            firestore.collection("users")
                    .document(auth.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // This will be used to send daily reports / weekly reports
                                email = documentSnapshot.getString("email");
                                if (email != null) {
                                    System.out.println("User's Email: " + email);
                                } else {
                                    System.out.println("Email field is null");
                                }
                            } else {
                                System.out.println("Document does not exist");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Handle failure
                            System.out.println("Error: " + e.getMessage());
                        }
                    });

            //  This block enters the user's step count data for the day.
            firestore.collection("users")
                    .document(auth.getUid())
                    .collection("StepCounter")
                    .document(newDocumentId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener < Void > () {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data sent successfully
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }

        // Stop the service
        stopSelf();

        return START_NOT_STICKY;
    }

    private long getLastResetTimeFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getLong(LAST_RESET_KEY, 0);
    }
    private int getStepCountFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(STEP_COUNT_KEY, 0);
    }
    private long getMidnightTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}