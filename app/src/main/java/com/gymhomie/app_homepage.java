package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class app_homepage extends AppCompatActivity {


    private TextView userNameView;
    private String userNameinDB;
    private FirebaseAuth authedProfile;
    private Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_homepage);

        userNameView = findViewById(R.id.userProfileName);
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authedProfile.signOut();
                Intent backtoLogin = new Intent(app_homepage.this, login_activity.class);
                startActivity(backtoLogin);
                finish();
            }
        });

        authedProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authedProfile.getCurrentUser();
        if(firebaseUser == null)
        {
            Toast.makeText(this, "No Data can be accessed", Toast.LENGTH_SHORT).show();
        }
        else {

            welcomeUser(firebaseUser);

        }

    }

    private void welcomeUser(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();

        //Fetching from firebase realtime db
        DatabaseReference referecedProfile = FirebaseDatabase.getInstance().getReference("Registered User Details");

        referecedProfile.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AddFetchUserDetails fetchUserDetails = snapshot.getValue(AddFetchUserDetails.class);
                if(fetchUserDetails!=null)
                {
                    userNameinDB = fetchUserDetails.textFirstName;
                    userNameView.setText("Welcome back "+ userNameinDB+ " !");
                    
                }else {
                    Toast.makeText(app_homepage.this, "No DATA", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}