package com.gymhomie;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




import androidx.appcompat.app.AppCompatActivity;

public class Stopwatch_Activity extends AppCompatActivity {

    //Buttons
    private Button resetBtn;
    private Button startBtn;
    private Button stopBtn;
    //Textview
    private TextView Display;

    private Handler handler = new Handler();
    private boolean isRunning;
    private int seconds = 0;
    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timertool);
        resetBtn = findViewById(R.id.stopwatch_reset);
        stopBtn = findViewById(R.id.stopwatch_stop);
        startBtn = findViewById(R.id.stopwatch_start);
        Display = findViewById(R.id.stopwatch_displayedText);
        isRunning = false;
        serviceIntent = new Intent(getApplicationContext(), TimerService.class);
        registerReceiver(updateTime, IntentFilter(TimerService.Time_Updcated));
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStopWatch();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopStopWatch();
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopWatch();
            }
        });


    }
    private BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            time = intent.getDoubleExtra(TimerService.TimeExtra)
            // Your code for handling the broadcast receiver's onReceive method here
        }
    };
    private Runnable runTimer = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                seconds++;
                updateDisplay();
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void updateDisplay() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
        Display.setText(time);
    }
    public void resetStopWatch(){
        isRunning = false;
        seconds = 0;
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        updateDisplay();
    }
    public void startStopWatch(){
        isRunning = true;
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        handler.postDelayed(runTimer, 1000);
    }
    public void stopStopWatch(){
        isRunning = false;
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);

        handler.removeCallbacks(runTimer);
    }
}
