package com.gymhomie;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.tools.GymReminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.Manifest;

//TODO: Need the workout type for Gym Reminder
public class Gym_Reminder_Activity extends AppCompatActivity implements GymReminderAdapter.OnCancelClickListener{
    private static final String KEY_OBJ = "gymreminder";
    private TimePicker timePicker;
    private Spinner dayPicker;
    private EditText gymReminderMessage;
    private Button saveReminderButton;
    private Button viewRemindersButton;
    private Button deleteRemindersButton;
    private GymReminder gymReminder;
    private ArrayList<GymReminder> gymReminderList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        deleteRemindersButton = findViewById(R.id.delete_reminders_button);

        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  saveNote(view);
                setAlarm(view);
            }
        });
        viewRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_view_reminders);
                viewReminders(view);
            }
        });
        deleteRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReminders();
            }
        });

    }

    private void viewReminders(View view) {
        // grab the gym reminders for current user
        //  ArrayList<GymReminder> gymReminderList = new ArrayList<>();
        gymReminderList = new ArrayList<>();
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            // int notificationID = ((Long) gymReminder.get("notificationID")).intValue();
                            Map<String, Object> gymReminderMap = (Map<String, Object>) documentSnapshot.get("gymreminder");
                            String workoutType = (String) gymReminderMap.get("workoutType");
                            String dayOfWeek = (String) gymReminderMap.get("dayOfWeek");
                            int reminderTimeHour = ((Long) gymReminderMap.get("reminderTimeHour")).intValue();
                            int reminderTimeMinute = ((Long) gymReminderMap.get("reminderTimeMinute")).intValue();
                            int notificationID = ((Long) gymReminderMap.get("notificationID")).intValue();

                            GymReminder gymReminder = new GymReminder(workoutType, dayOfWeek, reminderTimeHour, reminderTimeMinute, notificationID);
                            gymReminderList.add(gymReminder);

                        }
                        // update UI
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        GymReminderAdapter adapter = new GymReminderAdapter(getApplicationContext(), gymReminderList);
                        adapter.setOnCancelClickListener(new GymReminderAdapter.OnCancelClickListener() {
                            @Override
                            public void onCancelClick(int position) {
                                Log.d("Gym Reminder Activity", "onCancelClick method called");
                                GymReminder clickedReminder = gymReminderList.get(position);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                //  CollectionReference collectionReference = db.collection(collectionPath);
                                db.collection(collectionPath).get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                    Map<String, Object> gymReminder = (Map<String, Object>) documentSnapshot.get("gymreminder");
                                                    if (gymReminder == null) {
                                                        Log.d("Gym Reminder Deletion", "got null gym reminder");
                                                        continue;
                                                    }
                                                    int notificationID = ((Long) gymReminder.get("notificationID")).intValue();
                                                    if (notificationID == clickedReminder.getNotificationID()) {
                                                        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                                                        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationID, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
                                                        db.collection(collectionPath).document(documentSnapshot.getId()).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        //document successfully deleted, need to cancel the reminder
                                                                        alarmManager.cancel(pendingAlarmIntent);
                                                                        Toast.makeText(Gym_Reminder_Activity.this, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
                                                                        Log.d("Gym Reminder Deletion", "Successful deletion");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // TODO: Handle errors that occur during deletion
                                                                        Log.e("Gym Reminder Deletion", "Failure deletion");
                                                                    }
                                                                });
                                                    }

                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // TODO: handle errors that occur when fetching the documents
                                            }
                                        });
                            }
                        });

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: error handling
                    }
                });


    }

    private void deleteReminders() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //  CollectionReference collectionReference = db.collection(collectionPath);
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> gymReminder = (Map<String, Object>) documentSnapshot.get("gymreminder");
                            if (gymReminder == null) {
                                Log.d("Gym Reminder Deletion", "got null gym reminder");
                                continue;
                            }

                            int notificationID = ((Long) gymReminder.get("notificationID")).intValue();
                            Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                            PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationID, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
                            db.collection(collectionPath).document(documentSnapshot.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //document successfully deleted, need to cancel the reminder
                                            alarmManager.cancel(pendingAlarmIntent);
                                            Toast.makeText(Gym_Reminder_Activity.this, "All Reminders Cancelled", Toast.LENGTH_SHORT).show();
                                            Log.d("Gym Reminder Deletion", "Successful deletion");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // TODO: Handle errors that occur during deletion
                                            Log.e("Gym Reminder Deletion", "Failure deletion");
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: handle errors that occur when fetching the documents
                    }
                });
    }

    public void saveNote(View view, int notificationID) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String day = dayPicker.getSelectedItem().toString();
        String message = gymReminderMessage.getText().toString();

        gymReminder = new GymReminder(message, day, hour, minute, notificationID);
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_OBJ, gymReminder);

        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Gym_Reminder_Activity.this, "Reminder Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Gym_Reminder_Activity.this, "Error Saving Note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(View view) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String day = dayPicker.getSelectedItem().toString();
        String message = gymReminderMessage.getText().toString();
        int uniqueNotificationID = generateUniqueId();

        AlarmManager alarmMgr;
        PendingIntent pendingAlarmIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (day) {
            case "Monday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case "Tuesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case "Wednesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case "Thursday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case "Friday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case "Saturday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case "Sunday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long triggerTime = calendar.getTimeInMillis();
        //  Log.d("triggerAtMillis", String.valueOf(triggerTime));
        //  long intervalMillis = 604800000; // Should be one week
        long intervalMillis = 60000; // To test one minute intervals
        //  whole week delay? 453600000

        if (triggerTime <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            triggerTime = calendar.getTimeInMillis();
        }

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("notification_message", message);
        notificationIntent.putExtra("notification_ID", uniqueNotificationID);
        pendingAlarmIntent = PendingIntent.getBroadcast(this, uniqueNotificationID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        if (checkNotificationPermission()) {
            if (checkAlarmPermission()) {
                //  alarmMgr.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingAlarmIntent);
                saveNote(view, uniqueNotificationID);
                //  alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingAlarmIntent);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingAlarmIntent);
            }
        }
    }

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
    private boolean checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1);
            return false;
        }
        return true;
    }
    public static int generateUniqueId() {
        Random random = new Random();
        // Generate a random integer between 0 and 999999999 (inclusive)
        int randomNumber = random.nextInt(1000000000);
        // Ensure the number has 10 digits by adding leading zeros
        String randomString = String.format("%010d", randomNumber);
        // Parse the string as an integer
        return Integer.parseInt(randomString);
    }

    @Override
    public void onCancelClick(int position) {
        Log.d("Gym Reminder Activity", "onCancelClick method called");
        GymReminder clickedReminder = gymReminderList.get(position);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //  CollectionReference collectionReference = db.collection(collectionPath);
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> gymReminder = (Map<String, Object>) documentSnapshot.get("gymreminder");
                            if (gymReminder == null) {
                                Log.d("Gym Reminder Deletion", "got null gym reminder");
                                continue;
                            }
                            int notificationID = ((Long) gymReminder.get("notificationID")).intValue();
                            if (notificationID == clickedReminder.getNotificationID()) {
                                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                                PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationID, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
                                db.collection(collectionPath).document(documentSnapshot.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //document successfully deleted, need to cancel the reminder
                                                alarmManager.cancel(pendingAlarmIntent);
                                                Log.d("Gym Reminder Deletion", "Successful deletion");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // TODO: Handle errors that occur during deletion
                                                Log.e("Gym Reminder Deletion", "Failure deletion");
                                            }
                                        });
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: handle errors that occur when fetching the documents
                    }
                });
    }
}
