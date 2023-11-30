package com.gymhomie.workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

//workout will be displayed and user can check-off exercises as they finish them?
//finish button will log workout for the current date and bring user back to workout fragment
//if finish button is not clicked, workout will not be logged/will not show up in history

public class Start_Workout_Activity extends AppCompatActivity {
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_WORKOUT = "workout";
    private static final int REQUEST_CODE_START_WORKOUT = 98;
    private TextView workoutName, muscleGroups;
    private RecyclerView exercises;
    private Button finish;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/Workout History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.started_workout);

        workoutName = findViewById(R.id.workout_Name);
        muscleGroups = findViewById(R.id.muscle_Groups);
        exercises = findViewById(R.id.exerciseRecycler);
        finish = findViewById(R.id.finish);


        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //associate current date with this workout
                //stored in database under "Workout History" document

                //workout.completed_workout thisWorkout =

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userID = auth.getCurrentUser().getUid();
                String collectionPath = "users/"+userID+"/Workout History";

                Map<String, Object> note = new HashMap<>();

                db.collection(collectionPath).document(workoutName.getText().toString()).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Start_Workout_Activity.this, "Note saved: ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Start_Workout_Activity.this, "Error saving note!", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_START_WORKOUT && resultCode == RESULT_OK) {
            workout newWorkout = data.getParcelableExtra("newWorkout");
            String wn = newWorkout.getName();
            workoutName.setText(newWorkout.getName());
            muscleGroups.setText(newWorkout.getMuscleGroups().toString());
        }
    }
}
