package com.gymhomie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gymhomie.service.StepCountUploadService;

import java.util.Calendar;
import java.util.TimeZone;

public class MidnightResetReceiver extends BroadcastReceiver {

    private static final String PREF_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "stepCount";
    private static final String LAST_RESET_KEY = "lastResetTime";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Reset the step count
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(STEP_COUNT_KEY, 0);

        // Update the last reset time to today's midnight
        long midnightTime = getMidnightTime();
        editor.putLong(LAST_RESET_KEY, midnightTime);
        editor.apply();

        // Call the StepCountUploadService to upload the data to Firestore
        Intent serviceIntent = new Intent(context, StepCountUploadService.class);
        context.startService(serviceIntent);
    }

    private long getMidnightTime() {
            Calendar calendar = Calendar.getInstance();
        TimeZone userTimeZone = TimeZone.getDefault();
        calendar.setTimeZone(userTimeZone);

            if (userTimeZone != null) {
                calendar.setTimeZone(userTimeZone);
            }

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTimeInMillis();

    }
}

