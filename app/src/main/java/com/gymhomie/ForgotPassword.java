package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    FirebaseAuth auth;
    private Button resetButton;
    private EditText editText;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        resetButton = findViewById(R.id.resetButtonID);

        editText = findViewById(R.id.emaileditID);

         auth = FirebaseAuth.getInstance();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email= editText.getText().toString();

                if(!TextUtils.isEmpty(email))
                {
                    ResetPassword();
                }

                else {
                    editText.setError("Enter your email");
                }
                //auth.sendPasswordResetEmail(email);
                //

            }
        });

    }

    private void ResetPassword() {

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ForgotPassword.this, "Check your email for password reset instructions", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPassword.this, Login_activity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ForgotPassword.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}