package com.gymhomie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Step_Activity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView stepCountTextView;

    private TextView dateView;
    // Variables for step counting
    private int stepCount = 0;
    private Date today;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private static final float ALPHA = 0.8f;
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

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle the case when the accelerometer sensor is not available on the device
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

            // Check if a step is detected based on threshold and time delay
            long currentTime = System.nanoTime();
            if (magnitude > STEP_THRESHOLD && (currentTime - lastStepTime) > STEP_DELAY_NS) {
                stepCount++;
                lastStepTime = currentTime;
                updateStepCountUI();
            }
        }
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
            }
        });
    }
}
