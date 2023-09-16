package com.gymhomie;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.receiver.MidnightResetReceiver;
import com.gymhomie.service.DataEntryService;
import com.gymhomie.service.StepCountUploadService;
import com.gymhomie.tools.StepCounter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Step_Activity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView stepCountTextView;
    private Button startDataEntryButton;
    private MidnightResetReceiver midnightResetReceiver;
    private TextView dateView;
    // Variables for step counting
    private int stepCount = 0;
    private static final String PREF_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "stepCount";
    private static final String LAST_RESET_KEY = "lastResetTime";
    private Date today;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private static final float ALPHA = 0.8f;
    // variable for our bar chart

    // variable for our bar data.
    private BarData barData;

    // variable for our bar data set.
    private BarDataSet barDataSet;
    private static final int STEP_THRESHOLD = 4; // Adjust this threshold as needed
    private static final int STEP_DELAY_NS = 250000000; // Minimum time between steps (adjust as needed)
    private long lastStepTime = 0;
    private SimpleDateFormat dateFormat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        stepCountTextView = findViewById(R.id.accelerometerDataTextView);
        dateView = findViewById(R.id.todaysDateTextView);
        dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        today = new Date();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Load the previous step count from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        stepCount = prefs.getInt(STEP_COUNT_KEY, 0);

        // Check if the last reset time is before today's midnight, and reset the count if necessary
        long lastResetTime = prefs.getLong(LAST_RESET_KEY, 0);
        long midnightTime = getMidnightTime(today);

        if (savedInstanceState != null) {
            stepCount = savedInstanceState.getInt("stepCount", 0);
            updateStepCountUI();
        }

        if (lastResetTime < midnightTime) {
            stepCount = 0;
            // Update the last reset time to today's midnight
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(LAST_RESET_KEY, midnightTime);
            editor.apply();
        }

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle the case when the accelerometer sensor is not available on the device
        }

        // Schedule the alarm to trigger at midnight
        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        startDataEntryButton = findViewById(R.id.startDataEntryButton);

        // Set an OnClickListener for the button
        startDataEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBarEntries(v);
                setContentView(R.layout.activity_step_history);
            }
        });
    }
    private void getBarEntries(View v) {

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
                                items.add(String.valueOf(util.getSteps()));
//                                items.add(util.getDateString());
//                                items.add(String.valueOf(util.feetTravelled()));
//                                items.add(String.valueOf(util.milesTravelled()));
                                list.add(items);
                            }
                            ArrayList<Float> stepsRes = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                stepsRes.add(Float.parseFloat(list.get(i).get(0)));
                            }
//                            ArrayList<Float> dateRes = new ArrayList<>();
//                            for (int i = 0; i < list.size(); i++) {
//                                dateRes.add(Float.parseFloat(list.get(i).get(0)));
//                            }
//                            ArrayList<Float> feetRes = new ArrayList<>();
//                            for (int i = 0; i < list.size(); i++) {
//                                feetRes.add(Float.parseFloat(list.get(i).get(1)));
//                            }
//                            ArrayList<Float> milesRes = new ArrayList<>();
//                            for (int i = 0; i < list.size(); i++) {
//                                milesRes.add(Float.parseFloat(list.get(i).get(2)));
//                            }
//
//                            float sunday = dateRes.get(0);
//                            float monday = dateRes.get(1);
//                            float tuesday = dateRes.get(2);
//                            float wednesday = dateRes.get(3);
//                            float thursday = dateRes.get(4);
//                            float friday = dateRes.get(5);
//                            float saturday = dateRes.get(6);
//
//                            float sunday_feet = feetRes.get(0);
//                            float monday_feet = feetRes.get(1);
//                            float tuesday_feet = feetRes.get(2);
//                            float wednesday_feet = feetRes.get(3);
//                            float thursday_feet = feetRes.get(4);
//                            float friday_feet = feetRes.get(5);
//                            float saturday_feet = feetRes.get(6);


                            ArrayList barEntriesArrayList = new ArrayList<>();
                            // adding new entry to our array list with bar
                            // entry and passing x and y axis value to it.
                            barEntriesArrayList.add(new BarEntry(1, stepsRes.get(0)));
                            barEntriesArrayList.add(new BarEntry(2, stepsRes.get(1)));
                            barEntriesArrayList.add(new BarEntry(3, stepsRes.get(2)));
                            barEntriesArrayList.add(new BarEntry(4, stepsRes.get(3)));
                            barEntriesArrayList.add(new BarEntry(5, stepsRes.get(4)));
                            barEntriesArrayList.add(new BarEntry(6, stepsRes.get(5)));
                            barEntriesArrayList.add(new BarEntry(7, stepsRes.get(6)));

                            BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Weekly Step Analytics");

                            // creating a new bar data and
                            // passing our bar data set.
                            barData = new BarData(barDataSet);

                            // below line is to set data
                            // to our bar chart.

                            BarChart barChart = findViewById(R.id.idBarChart);
                            ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);
                            xAxis.setLabelCount(7);
                            xAxis.setValueFormatter(xAxisFormatter);
                            barChart.setData(barData);

                            // adding color to our bar data set.
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                            // setting text color.
                            barDataSet.setValueTextColor(Color.BLACK);

                            // setting text size
                            barDataSet.setValueTextSize(16f);
                            barChart.getDescription().setEnabled(false);
                            barChart.invalidate();
                        }
                    });

    }
    public void startDataEntryService() {
        Intent dataEntryIntent = new Intent(this, DataEntryService.class);
        startService(dataEntryIntent);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("stepCount", stepCount);
    }
    @Override
    protected void onResume() {
        super.onResume();

        midnightResetReceiver = new MidnightResetReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK); // This event occurs once per minute
        registerReceiver(midnightResetReceiver, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (midnightResetReceiver != null) {
            unregisterReceiver(midnightResetReceiver);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Update gravity values using a low-pass filter (exponential moving average)
            gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
            gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
            gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

            // Calculate linear acceleration
            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];

            // Calculate magnitude of linear acceleration
            float magnitude = (float) Math.sqrt(
                    linearAcceleration[0] * linearAcceleration[0] +
                            linearAcceleration[1] * linearAcceleration[1] +
                            linearAcceleration[2] * linearAcceleration[2]
            );

            // Update step count based on magnitude
            if (magnitude > STEP_THRESHOLD) {
                long currentTime = System.nanoTime();
                long timeDifference = currentTime - lastStepTime;

                // Check if a step has been taken, considering the time delay
                if (timeDifference > STEP_DELAY_NS) {
                    stepCount++;
                    lastStepTime = currentTime;
                    updateStepCountUI();
                }
            }

            // Check if midnight has passed (using current time)
            long currentTimeMillis = System.currentTimeMillis();
            long lastResetTime = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getLong(LAST_RESET_KEY, 0);
            long midnightTimeMillis = getMidnightTime(today);

            if (currentTimeMillis >= midnightTimeMillis && currentTimeMillis - lastResetTime >= 24 * 60 * 60 * 1000) {
                // Reset the step count and update the last reset time
                stepCount = 0;
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(STEP_COUNT_KEY, stepCount);
                editor.putLong(LAST_RESET_KEY, midnightTimeMillis);
                editor.apply();

                // Call the StepCountUploadService to upload the data to Firestore
                Intent serviceIntent = new Intent(this, StepCountUploadService.class);
                startService(serviceIntent);
            }
        }
    }
    private long getLastResetTimeFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getLong(LAST_RESET_KEY, 0);
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle changes in sensor accuracy if needed
    }
    private void updateStepCountUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stepCountTextView.setText(String.valueOf(stepCount));
                String formattedDate = dateFormat.format(today);
                dateView.setText("Today's Date:\n" + formattedDate);

                // Save the current step count and last reset time in SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(STEP_COUNT_KEY, stepCount);
                editor.putLong(LAST_RESET_KEY, getMidnightTime(today));
                editor.apply();
            }
        });
    }
    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            return "Day" + String.valueOf(Float.parseFloat(String.valueOf(value)));
        }
    }
}
