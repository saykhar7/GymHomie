package com.gymhomie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the alarm goes off.
        Log.d("AlarmReceiver", "Received alarm broadcast for gym reminder");
        String notificationMessage = intent.getStringExtra("notification_message");
        showNotification(context, notificationMessage);
    }

    //TODO: We need permissions somehow...never asks or throws error...had to manually change on device
    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String message) {
        createNotificationChannel(context);
        // Create a notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_gym_reminder")
                .setSmallIcon(R.drawable.stopwatch_icon)
                .setContentTitle("Gym Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id_gym_reminder",
                    "Channel Name: Gym Reminder Channel Name",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel Description: Gym Reminder/Alarm");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
