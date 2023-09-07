package com.gymhomie;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gymhomie.stepcounter.StepService;
import com.gymhomie.stepcounter.UpdateUiCallBack;

import java.util.Date;

public class Tool_Activity extends AppCompatActivity {

    private TextView tv_step;
    private Button btn_reset;
    private StepService mService;
    private boolean mIsRunning;
    private SharedPreferences mySharedPreferences;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                tv_step.setText(mySharedPreferences.getString("steps","0"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        tv_step = (TextView) findViewById(R.id.steps);
        btn_reset = (Button) findViewById(R.id.reset_btn);
        mySharedPreferences = getSharedPreferences("relevant_data", Activity.MODE_PRIVATE);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null) {
                    mService.resetValues();
                }
                tv_step.setText(mySharedPreferences.getString("steps", "0"));
            }
        });
        startStepService();
        bindStepService(); // Bind to the service immediately after starting it.
    }


    protected void onDestroy() {
        super.onDestroy();
//        stopStepService();
    }

    protected void onPause() {
        unbindStepService();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        tv_step.setText(mySharedPreferences.getString("steps", "0"));
        if (this.mIsRunning){
            bindStepService();
        }
    }



    private UpdateUiCallBack mUiCallback = new UpdateUiCallBack() {
        @Override
        public void updateUi() {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService.StepBinder binder = (StepService.StepBinder) service;
            mService = binder.getService();
            mService.registerCallback(mUiCallback);

            // Update the UI with the current step count when the service is connected.
            tv_step.setText(mySharedPreferences.getString("steps", "0"));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Handle disconnection if needed.
        }
    };


    private void bindStepService() {
        bindService(new Intent(this, StepService.class), this.mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindStepService() {
        unbindService(this.mConnection);
    }

    private void startStepService() {
        this.mIsRunning = true;
        startService(new Intent(this, StepService.class));
    }

    private void stopStepService() {
        this.mIsRunning = false;
        if (this.mService != null)
            stopService(new Intent(this, StepService.class));
    }

//    private SensorManager sensorManager;
//    private Sensor accelerometer;
//    private TextView accelerometerDataTextView;
//
//    // Variables for step counting
//    private int stepCount = 0;
//    private float[] gravity = new float[3];
//    private float[] linearAcceleration = new float[3];
//    private static final int STEP_THRESHOLD = 10; // Adjust this threshold as needed
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_steps);
//
//        accelerometerDataTextView = findViewById(R.id.accelerometerDataTextView);
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//        if (accelerometer != null) {
//            sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        } else {
//            // Handle the case when the accelerometer sensor is not available on the device
//        }
//    }
//
//    @Override
//    public void onSensorChanged(@NonNull SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            // Update gravity values
//            final float alpha = 0.8f;
//            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//
//            // Calculate linear acceleration
//            linearAcceleration[0] = event.values[0] - gravity[0];
//            linearAcceleration[1] = event.values[1] - gravity[1];
//            linearAcceleration[2] = event.values[2] - gravity[2];
//
//            // Calculate magnitude of linear acceleration
//            float magnitude = (float) Math.sqrt(
//                    linearAcceleration[0] * linearAcceleration[0] +
//                            linearAcceleration[1] * linearAcceleration[1] +
//                            linearAcceleration[2] * linearAcceleration[2]
//            );
//
//            // Check if a step is detected
//            if (magnitude > STEP_THRESHOLD) {
//                stepCount++;
//                updateStepCountUI();
//            }
//        }
//    }
//
//    private Double getSensorValue(SensorEvent event) {
//        return Math.sqrt(event.values[0] * event.values[0] +
//                event.values[1] * event.values[1] +
//                event.values[2] * event.values[2]);
//    }
//
//    /**
//     * Gets event time. http://stackoverflow.com/questions/5500765/accelerometer-sensorevent-timestamp
//     */
//    private long getTimestamp(SensorEvent event) {
//        return (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Handle changes in sensor accuracy if needed
//    }
//
//    private void updateStepCountUI() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                accelerometerDataTextView.setText("Steps: " + stepCount);
//            }
//        });
//    }

}
