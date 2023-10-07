package com.gymhomie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.tools.GymReminder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;

//TODO: Need the workout type for Gym Reminder
public class Gym_Reminder_Activity extends AppCompatActivity {
    private static final String KEY_OBJ = "gymreminder";
    private TimePicker timePicker;
    private Spinner dayPicker;
    private EditText gymReminderMessage;
    private Button saveReminderButton;
    private Button viewRemindersButton;
    private GymReminder gymReminder;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/GymReminders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gym_reminder);

        timePicker = findViewById(R.id.time_picker);
        dayPicker = findViewById(R.id.day_spinner);
        gymReminderMessage = findViewById(R.id.gym_reminder_message);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.days_of_week_array, // Create an array resource containing day names
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayPicker.setAdapter(adapter);

        saveReminderButton = findViewById(R.id.save_reminder_button);
        viewRemindersButton = findViewById(R.id.view_reminders_button);

        saveReminderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {saveNote(view); }
        });

    }

    public void saveNote(View v) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String day = dayPicker.getSelectedItem().toString();
        String message = gymReminderMessage.getText().toString();

        AlarmManager alarmMgr;
        PendingIntent pendingAlarmIntent;

        // TODO: Don't hardcode FRIDAY
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();
        Log.d("triggerAtMillis", String.valueOf(triggerTime));
        long intervalMillis = 604800000; // Should be one week
        //  long intervalMillis = 60000;

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("notification_message", message);
        pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        //TODO: ensure repeating works
        if (checkAlarmPermission()) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingAlarmIntent);
            //  alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalMillis, pendingAlarmIntent);
            //alarmMgr.cancel(pendingAlarmIntent);
        }

        gymReminder = new GymReminder(message, day, hour, minute);
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_OBJ, gymReminder);

        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Gym_Reminder_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Gym_Reminder_Activity.this, "Error Saving Note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //TODO: maybe need to ask for a different perm that relates to general notis on an app
    private boolean checkAlarmPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM},
                    1);
            return false;
        }
        return true;
    }
}
