package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Color;
import com.gymhomie.R;
import com.gymhomie.tools.WaterIntake;

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

    private static final String TAG = "Water_Intake_Activity";

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_AMOUNT = "amount";

    private NumberPicker amountPicker;

    private DatePicker datePicker;
    private Button waterSave;

    private Button currentMonthIntake;
    //db variables for storing and retrieval
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/WaterIntakes"; //path for the water intakes on firestore

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
        note.put(KEY_DAY, day);
        note.put(KEY_AMOUNT, amount);

        //storing in db
        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Water_Intake_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
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
}