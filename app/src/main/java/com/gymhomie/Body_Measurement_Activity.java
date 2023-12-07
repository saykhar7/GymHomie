package com.gymhomie;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.workouts.WorkoutAdapter;
import com.gymhomie.workouts.Workout_Activity;
import com.gymhomie.workouts.exercise;
import com.gymhomie.workouts.workout;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//when created, looks through database for values and shows them
//text is editable and user can input new values and press save to save them in the database

public class Body_Measurement_Activity extends AppCompatActivity {

    EditText left_bicep_size, right_bicep_size, left_forearm_size, right_forearm_size,
            left_thigh_size, right_thigh_size, left_calf_size, right_calf_size,
            shoulder_size, waist_size;
    Button save_button;
    ArrayList<String> measurement_list = new ArrayList<String>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String userMeasurementsPath = "users/" + userID + "/Body Measurements";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurementv2);

        left_bicep_size = findViewById(R.id.left_bicep_size);
        right_bicep_size = findViewById(R.id.right_bicep_size);
        left_forearm_size = findViewById(R.id.left_forearm_size);
        right_forearm_size = findViewById(R.id.right_forearm_size);
        left_thigh_size = findViewById(R.id.left_thigh_size);
        right_thigh_size = findViewById(R.id.right_thigh_size);
        left_calf_size = findViewById(R.id.left_calf_size);
        right_calf_size = findViewById(R.id.right_calf_size);
        shoulder_size = findViewById(R.id.shoulder_size);
        waist_size = findViewById(R.id.waist_size);
        save_button = findViewById(R.id.save_button);

        getMeasurements();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savenote();
                finish();
            }
        });
    }

    public void getMeasurements()
    {
        db.collection(userMeasurementsPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    //check if workouts exist
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            if (!documentSnapshot.exists()) {
                                continue;
                            }

                            left_bicep_size.setText((String) documentSnapshot.get("Left Bicep").toString());
                            right_bicep_size.setText((String) documentSnapshot.get("Right Bicep").toString());
                            left_forearm_size.setText((String) documentSnapshot.get("Left Forearm").toString());
                            right_forearm_size.setText((String) documentSnapshot.get("Right Forearm").toString());
                            left_thigh_size.setText((String) documentSnapshot.get("Left Thigh").toString());
                            right_thigh_size.setText((String) documentSnapshot.get("Right Thigh").toString());
                            left_calf_size.setText((String) documentSnapshot.get("Left Calf").toString());
                            right_calf_size.setText((String) documentSnapshot.get("Right Calf").toString());
                            shoulder_size.setText((String) documentSnapshot.get("Shoulders").toString());
                            waist_size.setText((String) documentSnapshot.get("Waist").toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Measurement Viewing Retrieval", "Failure to retrieve user measurements");
                    }
                });
    }

    public void savenote()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        String collectionPath = "users/"+userID+"/Body Measurements";

        Map<String, Object> note = new HashMap<>();
        note.put("Left Bicep", left_bicep_size.getText().toString());
        note.put("Right Bicep", right_bicep_size.getText().toString());

        note.put("Left Forearm", left_forearm_size.getText().toString());
        note.put("Right Forearm", right_forearm_size.getText().toString());

        note.put("Left Thigh", left_thigh_size.getText().toString());
        note.put("Right Thigh", right_thigh_size.getText().toString());

        note.put("Left Calf", left_calf_size.getText().toString());
        note.put("Right Calf", right_calf_size.getText().toString());

        note.put("Shoulders", shoulder_size.getText().toString());
        note.put("Waist", waist_size.getText().toString());


        db.collection(collectionPath).document("My Measurements").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Body_Measurement_Activity.this, "Note saved: ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Body_Measurement_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
