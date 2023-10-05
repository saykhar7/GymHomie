package com.gymhomie;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.*;


public class TimerService extends Service {

    private Timer timer = new Timer();
    private String TimeExtra = "TimeExtra";
    private String Time_Updated = "TimeUpdated";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int Flags, int startId){
        double time = intent.getDoubleExtra(TimeExtra, 0.0);
        timer.scheduleAtFixedRate(new TimeTask(time),0,1000);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        timer.cancel();
        super.onDestroy();
    }
        private  class TimeTask extends TimerTask{

            private double time;

            public TimeTask(double time) {
                this.time = time;
            }
            @Override
            public void run() {
            Intent intent = new Intent(Time_Updated);
            time++;
            intent.putExtra(TimeExtra, time);
            sendBroadcast(intent);
            }
        }

}
