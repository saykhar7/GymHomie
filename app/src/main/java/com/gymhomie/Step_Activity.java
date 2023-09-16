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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.gymhomie.receiver.MidnightResetReceiver;
import com.gymhomie.service.DataEntryService;
import com.gymhomie.service.StepCountUploadService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Step_Activity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView stepCountTextView;
    private Button startDataEntryButton;
    private MidnightResetReceiver midnightResetReceiver;
    private TextView dateView;
    private int stepCount = 0;
    private static final String PREF_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "stepCount";
    private static final String LAST_RESET_KEY = "lastResetTime";
    private Date today;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private static final float ALPHA = 0.8f;
    private BarData barData;
    private BarDataSet barDataSet;
    private static final float MIN_STEP_THRESHOLD = 1.5F; // Minimum threshold
    private static final float MAX_STEP_THRESHOLD = 10.0F; // Maximum threshold
    private float stepThreshold = MIN_STEP_THRESHOLD;
    private static final int MIN_STEP_DELAY_NS = 200000000; // Minimum time between steps
    private static final int MAX_STEP_DELAY_NS = 1000000000; // Maximum time between steps
    private long stepDelay = MAX_STEP_DELAY_NS;
    private long lastStepTime = 0;
    private SimpleDateFormat dateFormat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Initialize your UI components here, e.g., stepCountTextView
        stepCountTextView = findViewById(R.id.accelerometerDataTextView);
        dateView = findViewById(R.id.todaysDateTextView);
        dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        today = new Date();

        // Initialize sensor manager and accelerometer
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
        scheduleMidnightResetAlarm();

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
        // Your code for retrieving bar entries and updating the chart
        // This part remains unchanged
    }

    private void scheduleMidnightResetAlarm() {
        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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
            if (magnitude > stepThreshold) {
                long currentTime = System.nanoTime();
                long timeDifference = currentTime - lastStepTime;

                // Check if a step has been taken, considering the time delay
                if (timeDifference > stepDelay) {
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

    public void startDataEntryService() {
        Intent dataEntryIntent = new Intent(this, DataEntryService.class);
        startService(dataEntryIntent);
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
            return "Day" + Math.round(value);
        }
    }

}

