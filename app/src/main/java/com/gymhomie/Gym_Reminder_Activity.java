package com.gymhomie;

import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.tools.GymReminder;

import java.util.HashMap;
import java.util.Map;

import kotlin._Assertions;

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
}
