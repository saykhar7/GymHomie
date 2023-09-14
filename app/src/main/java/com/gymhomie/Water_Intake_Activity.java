package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.R;
import com.gymhomie.tools.WaterIntake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Water_Intake_Activity extends AppCompatActivity{

    private static final String TAG = "Water_Intake_Activity";

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_AMOUNT = "amount";

    private NumberPicker amountPicker;

    private DatePicker datePicker;
    private Button waterSave;

    private Button currentMonthIntake;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/WaterIntakes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_water_intake);

        datePicker = findViewById(R.id.date_picker);
        amountPicker = findViewById(R.id.amount_number_picker);
        amountPicker.setMinValue(1);
        amountPicker.setMaxValue(128);
        amountPicker.setValue(1);

        waterSave = findViewById(R.id.water_save_button);
        waterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });
        currentMonthIntake = findViewById(R.id.current_month_intake);
        currentMonthIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Swap pages to view current month's water intake
                setContentView(R.layout.activity_water_intake_retrieval);
                retrieveMonthlyIntakes(view);
            }
        });

    }

    public void saveNote(View v) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth()+1;
        int day = datePicker.getDayOfMonth();
        int amount = amountPicker.getValue();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_YEAR, year);
        note.put(KEY_MONTH, month);
        note.put(KEY_DAY, day);
        note.put(KEY_AMOUNT, amount);

        db.collection(collectionPath).document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Water_Intake_Activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Water_Intake_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void retrieveMonthlyIntakes(View v) {
        TextView monthlyAverage = findViewById(R.id.monthly_average);
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int sum = 0;
                        int count = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Long month = document.getLong("month");
                            Long year = document.getLong("year");
                            if (year == currentYear & month == currentMonth) {
                                    Long amount = document.getLong("amount");
                                    sum += amount.intValue();
                                    count++;
                            }
                        }
                        double average = (sum*1.0) / count;
                        monthlyAverage.setText("Monthly Average: " + String.valueOf(average));
                    }
                });

    }


}