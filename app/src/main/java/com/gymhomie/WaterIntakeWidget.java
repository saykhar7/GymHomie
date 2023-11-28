package com.gymhomie;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

/**
 * Implementation of App Widget functionality.
 */
public class WaterIntakeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, double average) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.water_intake);
        views.setTextViewText(R.id.appwidget_text, "Today's Intake: " + String.format("%.2f", average)+"oz");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/WaterIntakes"; //path for the water intakes on firestore

        LocalDate currentDate = LocalDate.now(); //get the current date
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int currentDay = currentDate.getDayOfMonth();

        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int sum = 0; // so we can get the average, add all amounts
                        int count = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Long month = document.getLong("month");
                            Long year = document.getLong("year");
                            Long day = document.getLong("day");
                            // check if the entry has the current month and year, so we can add it and show user
                            if (year == currentYear && month == currentMonth && currentDay == day) {
                                float amount = (document.getLong("amount")).floatValue();
                                sum += amount;
                                count++;

                                // Assuming you still need this flag, otherwise, it's unnecessary
                                Boolean flag = false;
                            }
                        }


                        // Update the widget with the calculated average
                        for (int appWidgetId : appWidgetIds) {
                            updateAppWidget(context, appWidgetManager, appWidgetId, sum);
                        }
                    }
                });

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}