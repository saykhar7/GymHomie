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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_signup extends AppCompatActivity {

    private EditText email, email1, password, password1, fName, lName;
    private Button signUpbtn;
    private ProgressBar progressBarView;

    private FirebaseFirestore firebaseFirestore;
    private String userID;


    //TAG String for Exception Handling
    private static final String TAG = "SignUp Page: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       fName = findViewById(R.id.etFirstName);
       lName = findViewById(R.id.etLastName);

       email = findViewById(R.id.signEditEmail);
       email1 = findViewById(R.id.signEditEmail1);


       password = findViewById(R.id.signEditPassword);
       password1 = findViewById(R.id.signEditPassword1);

       progressBarView = findViewById(R.id.progressBar);





       Toast.makeText(this, "Please Register Here", Toast.LENGTH_SHORT).show();

       signUpbtn = findViewById(R.id.signUpMain);

       signUpbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String firstName = fName.getText().toString();
               String lastName = lName.getText().toString();

               String emailCheck = email.getText().toString();
               String emailCheck1 = email1.getText().toString();

               String passwordCheck1 = password.getText().toString();
               String passwordCheck = password1.getText().toString();


               String emailFinal;
               String passwordFinal;

               if (TextUtils.isEmpty(firstName)) {
                   Toast.makeText(Activity_signup.this, "Please Enter Your First Name", Toast.LENGTH_SHORT).show();
                   fName.setError("First Name Missing");
                   fName.requestFocus();
               } else if (TextUtils.isEmpty(lastName)) {
                   Toast.makeText(Activity_signup.this, "Please Enter Your Last Name", Toast.LENGTH_SHORT).show();
                   lName.setError("Last Name Missing");
               } else if (TextUtils.isEmpty(emailCheck)) {
                   Toast.makeText(Activity_signup.this, "", Toast.LENGTH_SHORT).show();
                   email.setError("Enter Email Address");


               } else if (TextUtils.isEmpty(emailCheck1)) {
                   Toast.makeText(Activity_signup.this, "", Toast.LENGTH_SHORT).show();
                   email1.setError("Enter Email Address");
                   email1.requestFocus();

               } else if (!emailCheck.equals(emailCheck1)) {
                   Toast.makeText(Activity_signup.this, "Emails do not match!", Toast.LENGTH_SHORT).show();
                   email.setError("Re-enter Email");
                   email1.setError("Re-enter Email");
                   email.requestFocus();
                   email1.requestFocus();


               } else if (!passwordCheck.equals(passwordCheck1)) {
                   Toast.makeText(Activity_signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                   password.setError("Re-enter password");
                   password1.setError("Re-Enter password");
                   password.requestFocus();
                   password1.requestFocus();

               }
               else {
                   emailFinal = emailCheck;
                   passwordFinal =passwordCheck;
                   progressBarView.setVisibility(View.VISIBLE);
                   registerUser(firstName, lastName, emailFinal, passwordCheck);

               }

           }


       });



    }

    //Adding user registration data to firebase DB
    private void registerUser(String firstName, String lastName, String emailFinal, String passwordCheck) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Adding user to firestore authentication database
        auth.createUserWithEmailAndPassword(emailFinal, passwordCheck).addOnCompleteListener(Activity_signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBarView.setVisibility(View.GONE);
                
                if(task.isSuccessful())
                {

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    firebaseFirestore = FirebaseFirestore.getInstance();


                    //Saving name, email to firebase realtime database

                    userID= auth.getCurrentUser().getUid();

                    Map<String,Object> aUser = new HashMap<>();
                    aUser.put("firstName", firstName);
                    aUser.put("lastName", lastName);
                    aUser.put("email", emailFinal);

                    //Making an object with reference of the Firestore Database Collection
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);


                    //On Successfull addition of user data to database, this listerner will get triggered
                    documentReference.set(aUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(Activity_signup.this, "Homie Registered", Toast.LENGTH_SHORT).show();


                            //Opening currently registered profile
                            Intent intent = new Intent(Activity_signup.this, Home_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //Closing the register page
                            finish();

                        }
                    });

//                    DatabaseReference userProfileReference = FirebaseDatabase.getInstance().getReference("Registered User Details");
//                    documentReference.child(firebaseUser.getUid()).setValue(adduserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if(task.isSuccessful())
//                            {
//                                firebaseUser.sendEmailVerification();
//
//
//                            }else {
//                                Toast.makeText(Activity_signup.this, "Homie Registation Failed", Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//
//                        }
//                    });

                }
                else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthUserCollisionException e)
                    {

                        Toast.makeText(Activity_signup.this, "Email already used", Toast.LENGTH_SHORT).show();
                        email.setError("Enter New Email");
                        email1.setError("Re-enter New Email");
                        email.requestFocus();
                        email1.requestFocus();

                    }catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage() );
                        Toast.makeText(Activity_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                

            }
        });

    }
}