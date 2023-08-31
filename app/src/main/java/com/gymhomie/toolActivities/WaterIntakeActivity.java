package com.gymhomie.toolActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.R;
import com.gymhomie.tools.WaterIntake;

import java.util.HashMap;
import java.util.Map;

public class WaterIntakeActivity extends AppCompatActivity{

    private static final String TAG = "WaterIntakeActivity";

    //  public WaterIntake newData = new WaterIntake("08-30-2023", "9:26PM", 16);

    private static final String KEY_DATE = "date";

    private static final String KEY_TIME= "time";

    private static final String KEY_AMOUNT = "amount";

    private EditText editTextDate;
    private EditText editTextTime;
    private EditText editTextAmount;

    private Button waterSave;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intake); // TODO Make a view for water intake

        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime = findViewById(R.id.edit_text_time);
        editTextAmount = findViewById(R.id.edit_text_amount);

        waterSave = findViewById(R.id.water_save_button);
        waterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });
    }

    public void saveNote(View v) {
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String amountAsString = editTextAmount.getText().toString();
        int amount;
        try {
            amount = Integer.parseInt(amountAsString);
        } catch (NumberFormatException e) {
            amount = -1;
        }

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_DATE, date);
        note.put(KEY_TIME, time);
        note.put(KEY_AMOUNT, amount);

        db.collection("Water Intakes").document("My First Water Intake").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(WaterIntakeActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WaterIntakeActivity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
