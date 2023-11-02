package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Water_Intake_Activity extends AppCompatActivity{

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_AMOUNT = "amount";

    private NumberPicker amountPicker;

    private DatePicker datePicker;
    private Button waterSave;

    private Button currentMonthIntake;
    //db variables for storing and retrieval
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/WaterIntakes"; //path for the water intakes on firestore
    String achievementCollectionPath = "users/"+userID+"/Achievements"; //path for the user's achievements on firestore


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_water_intake);

        datePicker = findViewById(R.id.date_picker);
        amountPicker = findViewById(R.id.amount_number_picker);

        //setting values for the number picker so user doesn't have to input
        amountPicker.setMinValue(1);
        amountPicker.setMaxValue(128);
        amountPicker.setValue(1);

        waterSave = findViewById(R.id.water_save_button);
        waterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });
        currentMonthIntake = findViewById(R.id.current_month_intake);
        currentMonthIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_water_intake_retrieval);
                retrieveMonthlyIntakes(view);
            }
        });

    }
    //saveNote is to store new water intakes made by user
    public void saveNote(View v) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth()+1;
        int day = datePicker.getDayOfMonth();
        int amount = amountPicker.getValue();

        //hash map for input to database
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_YEAR, year);
        note.put(KEY_MONTH, month);
        note.put(KEY_DAY, day);
        note.put(KEY_AMOUNT, amount);

        //storing in db
        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Water_Intake_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                        // update corresponding achievements, we pass current note so we only check the day that was changed
                        updateWaterAchievements(note);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Water_Intake_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //retrieveMonthlyIntakes is button for user to see graph of their current month's water intake
    public void retrieveMonthlyIntakes(View v) {
        LineChart lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> entries = new ArrayList<>(); // entries (x,y) for x = day, y = amount
        TextView monthlyAverage = findViewById(R.id.monthly_average);

        LocalDate currentDate = LocalDate.now(); //get the current date
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        //retrieve the water intakes for current user
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int sum = 0; //so we can get the average, add all amounts
                        int count = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Long month = document.getLong("month");
                            Long year = document.getLong("year");
                            //check if the entry has current month and year, so we can add it and show user
                            if (year == currentYear & month == currentMonth) {
                                    float amount = (document.getLong("amount")).floatValue();
                                    float day = (document.getLong("day")).floatValue();
                                    sum += amount;

                                    Boolean flag = false; //flag for compounding days with multiple water intakes (otherwise, two xs for one y)
                                    for (Entry entry : entries) {
                                        flag = false;
                                        if (entry.getX() == day) {
                                            entry.setY(entry.getY()+amount);
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag) {
                                        count++;
                                        entries.add(new Entry(day, amount));
                                }
                            }
                        }
                        double average = (sum*1.0) / count;
                        String averageString = String.format("%.2f", average);
                        monthlyAverage.setText("Daily Average: " + averageString + " oz"); //output average for user
                        //we need to sort the entries so the points connect from left to right
                        Collections.sort(entries, new Comparator<Entry>() {
                            @Override
                            public int compare(Entry entry1, Entry entry2) {
                                return Float.compare(entry1.getX(), entry2.getX());
                            }
                        });
                        LineDataSet dataSet = new LineDataSet(entries, "Water Intake");

                        //formatting for the datapoints
                        dataSet.setLineWidth(4f);
                        dataSet.setCircleHoleColor(R.color.black);
                        dataSet.setCircleColor(R.color.black);
                        dataSet.setCircleRadius(3f);
                        dataSet.setCircleHoleRadius(3f);
                        dataSet.setValueTextSize(9f);

                        //formatting the graph and axis
                        LineData lineData = new LineData(dataSet);
                        lineChart.setData(lineData);
                        lineChart.getDescription().setEnabled(false);
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        YAxis yAxisLeft = lineChart.getAxisLeft();
                        YAxis yAxisRight = lineChart.getAxisRight();
                        yAxisLeft.setGranularity(1f);

                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                return "Day " + ((int) value);
                            }
                        });
                        yAxisLeft.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                return ((int) value + "oz" );
                            }
                        });
                        yAxisRight.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                return ((int) value + "oz" );
                            }
                        });
                        lineChart.invalidate(); //update the graph for user
                    }
                });
    }
    public void updateWaterAchievements(Map<String, Object> newNote) {
        // TODO: only works for achievement 1, 2, 4 (novice, enthu, parched), need to think about others
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // let's check each user's water intake for same day, month, year as newNote
                        int totalAmount = 0;
                        for (DocumentSnapshot userWaterIntake: queryDocumentSnapshots.getDocuments()) {
                            int day = ((Long) userWaterIntake.get("day")).intValue();
                            int month = ((Long) userWaterIntake.get("month")).intValue();
                            int year = ((Long) userWaterIntake.get("year")).intValue();
                            int amount = ((Long) userWaterIntake.get("amount")).intValue();
                            if (((int)newNote.get(KEY_YEAR) == year) && ((int)newNote.get(KEY_MONTH) == month) && ((int)newNote.get(KEY_DAY) == day)) {
                                totalAmount += amount;
                            }
                        }

                        // update achievements 1 (hydration novice)
                        DocumentReference hydrationNovice = db.collection(achievementCollectionPath).document("1");
                        int finalTotalAmount = totalAmount;
                        hydrationNovice.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()) {
                                            Log.e("Water Intake Achievement Update", "hydrationNovice doc reference dne");
                                        }
                                        int currentProgress = ((Long) documentSnapshot.get("progress")).intValue();
                                        if (currentProgress < finalTotalAmount) { // first we check to not overwrite if already progressed further (a different date)
                                            hydrationNovice.update("progress", finalTotalAmount);
                                            int progressNeeded = ((Long) documentSnapshot.get("criteria")).intValue();
                                            if (!((boolean) documentSnapshot.get("unlocked"))) {
                                                if (finalTotalAmount >= progressNeeded) {
                                                    hydrationNovice.update("unlocked", true);
                                                    // queue achievement popup
                                                    Toast.makeText(Water_Intake_Activity.this, "Achievement Unlocked: Hydration Novice", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Water Intake Achievement Update", "Hydration novice (1) not found");
                                    }
                                });
                        // update achievements 2 (hydration enthusiast)
                        DocumentReference hydrationEnthusiast = db.collection(achievementCollectionPath).document("2");
                        hydrationEnthusiast.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()) {
                                            Log.e("Water Intake Achievement Update", "hydrationEnthusiast doc reference dne");
                                        }
                                        int currentProgress = ((Long) documentSnapshot.get("progress")).intValue();
                                        if (currentProgress < finalTotalAmount) {
                                            hydrationEnthusiast.update("progress", finalTotalAmount);
                                            int progressNeeded = ((Long) documentSnapshot.get("criteria")).intValue();
                                            if (!((boolean) documentSnapshot.get("unlocked"))) {
                                                if (finalTotalAmount >= progressNeeded) {
                                                    hydrationEnthusiast.update("unlocked", true);
                                                    // queue achievement popup
                                                    Toast.makeText(Water_Intake_Activity.this, "Achievement Unlocked: Hydration Enthusiast", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Water Intake Achievement Update", "Hydration Enthusiast (2) not found");
                                    }
                                });
                        // update achievements 4 (parched gym bro)
                        DocumentReference parchedGymBro = db.collection(achievementCollectionPath).document("4");
                        parchedGymBro.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()) {
                                            Log.d("Water Intake Achievement Update", "parchedGymBro doc reference dne");
                                        }
                                        parchedGymBro.update("progress", finalTotalAmount);
                                        int progressNeeded = ((Long) documentSnapshot.get("criteria")).intValue();
                                        if (!((boolean) documentSnapshot.get("unlocked"))) {
                                            if (finalTotalAmount < progressNeeded) {
                                                parchedGymBro.update("unlocked", true);
                                                // queue achievement popup
                                                Toast.makeText(Water_Intake_Activity.this, "Achievement Unlocked: Parched Gym Bro", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Water Intake Achievement Update", "Parched Gym Bro (4) not found");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Water Intake Achievement Update", "Error retrieving water intakes");
                    }
                });
    }
}