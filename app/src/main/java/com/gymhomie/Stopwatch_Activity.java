package com.gymhomie;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stopwatch_Activity extends AppCompatActivity {
    private Handler handler = new Handler();
    private TimerService timerService;
    private boolean isBound = false;
    private boolean isRunning = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TimerService.LocalBinder binder = (TimerService.LocalBinder) iBinder;
            timerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            timerService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timertool);

        //Button startButton = findViewById(R.id.stopwatch_start);
        //Button stopButton = findViewById(R.id.stopwatch_stop);
        Button resetButton = findViewById(R.id.stopwatch_reset);

        Button Display = findViewById(R.id.timerbutton);

        // Bind to the TimerService
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning){
                    if (isBound && timerService != null) {
                        timerService.stopTimer();
                        isRunning = !isRunning;
                    }
                }
                else{
                    if (isBound && timerService != null) {
                        timerService.startTimer();
                        isRunning = !isRunning;
                    }
                }
            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound && timerService != null) {
                    timerService.resetTimer();
                }
            }
        });

        // Update the TextView with elapsed time
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isBound && timerService != null) {
                    long elapsedTime = timerService.getElapsedTime();
                    Display.setText(formatDuration(elapsedTime));
                }
                handler.postDelayed(this, 1000); // Update every second
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
    public static String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // Format the values into "00:00:00" format
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
