package com.gymhomie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.stream.BaseStream;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the alarm goes off.

        String notificationMessage = intent.getStringExtra("notification_message");
        int notificationID = intent.getIntExtra("notification_ID", -1); //-1 if not provided properly
        String msg = "Received alarm broadcast for gym reminder" + notificationID;
        Log.d("AlarmReceiver", msg);
        showNotification(context, notificationMessage, notificationID);
    }

    //TODO: We need permissions somehow...never asks or throws error...had to manually change on device
    private void showNotification(Context context, String message, int notificationID) {
        createNotificationChannel(context);
        // Create a notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_gym_reminder")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Gym Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String msg = "calling alarm with notiID " + notificationID;
        Log.d("AlarmReceiver", msg);
        notificationManager.notify(notificationID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id_gym_reminder",
                    "Channel Name: Gym Reminder Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel Description: Gym Reminder/Alarm");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
