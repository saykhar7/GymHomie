package com.gymhomie.service;

import android.app.IntentService;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.R;
import com.gymhomie.tools.StepCounter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataEntryService extends IntentService {
    private String email;

    public DataEntryService() {
        super("DataEntryService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int stepCount = 100;
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
                        retrieveMonthlyIntakes();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });



        // Stop the service when done
        stopSelf();
    }
    public ArrayList<ArrayList<String>> retrieveMonthlyIntakes() {
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

}

