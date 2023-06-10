package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login_activity extends AppCompatActivity {

    private EditText emailAddress, loginPassword;
    private ProgressBar progressBarLogin;
    private FirebaseAuth authUser;
    private Button loginButton;
    private static final String TAG= "Login Level: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this, "Please enter your credentials for Login", Toast.LENGTH_SHORT).show();

        emailAddress = findViewById(R.id.loginEmailEdit);
        loginPassword = findViewById(R.id.loginPasswordEdit);
        loginButton = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        authUser = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String emailAddressText = emailAddress.getText().toString();
               String passwordText = loginPassword.getText().toString();

               if (TextUtils.isEmpty(emailAddressText))
               {
                   Toast.makeText(Login_activity.this, "Email ID missing", Toast.LENGTH_SHORT).show();
                   emailAddress.setError("Enter email address");
                   emailAddress.requestFocus();
               } else if (TextUtils.isEmpty(passwordText)) {
                   Toast.makeText(Login_activity.this, "Password missing", Toast.LENGTH_SHORT).show();
                   loginPassword.setError("Enter Password");
                   loginPassword.requestFocus();

               }
               else {

                   progressBarLogin.setVisibility(View.VISIBLE);
                   loginUser(emailAddressText, passwordText);
               }

            }
        });


    }


    //Checking if the use was logged in before
    @Override
    protected void onStart() {
        super.onStart();
        if(authUser.getCurrentUser() != null)
        {
            Toast.makeText(this, "You're alaready logged in", Toast.LENGTH_SHORT).show();

            Intent savedUserIntent = new Intent(Login_activity.this, Home_Activity.class);
            startActivity(savedUserIntent);

            //Opening the user profile
        }
        else {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String emailAddressText, String passwordText) {

        authUser.signInWithEmailAndPassword(emailAddressText, passwordText).addOnCompleteListener(Login_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBarLogin.setVisibility(View.GONE);
                
                if(task.isSuccessful())
                {
                    Toast.makeText(Login_activity.this, "You're logged in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Login_activity.this, Home_Activity.class);
                    startActivity(intent1);
                    finish();
                }
                else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e)
                    {
                        Toast.makeText(Login_activity.this, "No User on this email, please signup", Toast.LENGTH_SHORT).show();
                        emailAddress.clearComposingText();
                        emailAddress.requestFocus();
                        loginPassword.clearComposingText();
                        loginPassword.requestFocus();


                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        emailAddress.setError("Invalid Email");
                        emailAddress.requestFocus();

                        loginPassword.setError("Invalid password");
                        loginPassword.requestFocus();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Login_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                
            }
        });
    }
}