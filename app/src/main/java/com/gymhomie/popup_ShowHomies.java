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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.tools.Homie;

import java.util.ArrayList;

public class popup_ShowHomies extends Activity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userPath = "users";
    Homie homie = new Homie();
    ArrayList<String> homies = homie.getHomies();

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
                for (String homie : homies) {
                    Button button = new Button(popup_ShowHomies.this);
                    button.setText(homie);

                    // Set an OnClickListener for each button
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProfileConfirmationDialog(homie);
                        }
                    });

                    homiesContainer.addView(button);
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
    private void showProfileConfirmationDialog(String homieName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View Profile");
        builder.setMessage("Do you want to view " + homieName + "'s profile?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to view the profile
                // You can launch the profile activity or perform any other action here
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
}
