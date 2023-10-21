package com.gymhomie;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class TimerService extends Service {

    private final IBinder binder = new LocalBinder();
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            handler.post(runnable);
            isRunning = true;
        }
    }

    public void stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    public void resetTimer() {
        elapsedTime = 0;
        startTime = 0;
    }

    public long getElapsedTime() {
        return elapsedTime / 1000; // Return elapsed time in seconds
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
