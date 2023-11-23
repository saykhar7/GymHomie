package com.gymhomie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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

        // Populate the homiesContainer with TextViews


        // Find the "Show All" button
        Button showAllButton = findViewById(R.id.showAllButton);

        // Set an OnClickListener for the button
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Show All" button click
                for (String homie : homies) {
                    TextView textView = new TextView(popup_ShowHomies.this);
                    textView.setText(homie);

                    // You can customize the TextView properties here if needed

                    homiesContainer.addView(textView);
                }
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

}
