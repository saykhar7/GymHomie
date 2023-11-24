package com.gymhomie;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.tools.Homie;
import com.gymhomie.workouts.WorkoutHelper;

import java.util.ArrayList;
import java.util.Map;

public class popup_ShowHomies extends Activity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userPath = "users";
    Homie homie = new Homie();
    RecyclerView recyclerView;
    ArrayList<String> homies = homie.getHomies();
    ArrayList<String> paths = homie.getPaths();
    WorkoutHelper wh = new WorkoutHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_show_homies);

        LinearLayout homiesContainer = findViewById(R.id.homiesContainer);


        // Find the "Show All" button
        Button showAllButton = findViewById(R.id.showAllButton);

        // Set an OnClickListener for the button
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Populate the homiesContainer with Buttons
                int count = 0;
                for (String homie : homies) {
                    Button button = new Button(popup_ShowHomies.this);
                    button.setText(homie);
                    wh.setHomieWorkouts(paths.get(count));
                    ArrayList<Map<String, Object>> workouts = wh.getHomieWorkouts();
                    // Set an OnClickListener for each button
                    int finalCount = count;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProfileConfirmationDialog(workouts, homie, finalCount, v);
                        }
                    });

                    homiesContainer.addView(button);
                    count++;
                }
                // Handle the "Show All" button click
                showAllHomies();
            }
        });
    }

    // Define the behavior when the "Show All" button is clicked
    private void showAllHomies() {
        // Add your logic here to show all homies
        // For example, you can navigate to a new activity or update the UI accordingly
        Toast.makeText(this, "Showing all homies", Toast.LENGTH_SHORT).show();
    }

    // Show a confirmation dialog to view the homie's profile
    private void showProfileConfirmationDialog(ArrayList<Map<String, Object>> workouts, String homieName, int count, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View Profile");
        builder.setMessage("Do you want to view " + homieName + "'s profile?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to view the profile
                // You can launch the profile activity or perform any other action here
                // Now, you have a list of workout objects. You can update your UI here.
                updateUI(homieName, workouts, v);
                Toast.makeText(popup_ShowHomies.this, "Viewing " + homieName + "'s profile", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision not to view the profile
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void updateUI(String homieName, ArrayList<Map<String, Object>> workouts, View v) {
        // Inflate the homie_profile_view.xml layout
        View profileView = getLayoutInflater().inflate(R.layout.homie_profile, null);

        // Set the homieNameTextView
        TextView homieNameTextView = profileView.findViewById(R.id.homieNameTextView);
        homieNameTextView.setText(homieName);

        // Find the RecyclerView in the profileView
        RecyclerView recyclerView = profileView.findViewById(R.id.workoutsRecyclerView);

        // Create an instance of your custom RecyclerViewAdapter
        HomieProfileViewAdapter adapter = new HomieProfileViewAdapter(workouts);

        // Set the adapter to your RecyclerView
        recyclerView.setAdapter(adapter);

        // Optionally, you can set a layout manager to define how items are arranged
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Show the profileView in a dialog or any other way you want
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(profileView);
        builder.show();
    }

}
