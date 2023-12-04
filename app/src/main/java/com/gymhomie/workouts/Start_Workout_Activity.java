package com.gymhomie.workouts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.internal.runner.intent.IntentMonitorImpl;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

//class is called with "start" button on a certain recyclerview of a workout

//workout will be displayed (and user can check-off exercises as they finish them?)

//finish button will log workout for the current date in the database in the
//"Workout_History" collection and bring user back to workout fragment

//if finish button is not clicked, workout will not be logged/will not show up in history

//works 90%, selected workout needs to be grabbed (maybe workout id or whole object) and displayed

//database currently stores whatever is in the textview title

public class Start_Workout_Activity extends AppCompatActivity {
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_WORKOUT = "workout";
    private TextView workoutName, muscleGroups;
    private RecyclerView exercises;
    private Button finish;
    private workout myWorkout;
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        myWorkout = bundle.getParcelable("workout");
        workoutName.setText(myWorkout.getName());

        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //associate current date with this workout
                //stored in database under "Workout History" document

                //workout.completed_workout thisWorkout =

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userID = auth.getCurrentUser().getUid();
                String collectionPath = "users/"+userID+"/Workout History";

                //workout myWorkout = new workout(workoutName.getText().toString(), muscleGroups, exercises);
                Map<String, Object> note = new HashMap<>();
                note.put("Workout", myWorkout);

                /*
                note.put("Workout name", workoutName.getText().toString());
                note.put("Muscle Groups", muscleGroups);
                note.put("Exercises", exercises);
                */

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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        workout newWorkout = bundle.getParcelable("workout");
        //workout newWorkout = getIntent().getParcelableExtra("newWorkout");
        myWorkout = newWorkout;
        //String wn = newWorkout.getName();
        workoutName.setText((CharSequence) newWorkout.getName().toString());
        //workoutName.setText(newWorkout.getName().toString());
        muscleGroups.setText(newWorkout.getMuscleGroups().toString());


    }
}
