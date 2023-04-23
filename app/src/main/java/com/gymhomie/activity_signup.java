package com.gymhomie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_signup extends AppCompatActivity {

    private EditText email, email1, password, password1, firstName, lastName;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       firstName = findViewById(R.id.etFirstName);
       lastName = findViewById(R.id.etLastName);

       email = findViewById(R.id.signEditEmail);
       email1 = findViewById(R.id.signEditEmail1);


       password = findViewById(R.id.signEditPassword);
       password1 = findViewById(R.id.signEditPassword1);


       getSupportActionBar().setTitle("Register");

        Toast.makeText(this, "Please Register Here", Toast.LENGTH_SHORT).show();



    }
}