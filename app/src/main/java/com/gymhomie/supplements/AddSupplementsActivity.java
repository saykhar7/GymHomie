package com.gymhomie.supplements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.R;

public class AddSupplementsActivity extends AppCompatActivity {

    EditText supplementName, supplementDes;
    ImageView saveSuppelement;

    String name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplements);

        supplementName = findViewById(R.id.supplements_name_btnID);
        supplementDes = findViewById(R.id.supplements_description_btnID);

        saveSuppelement = findViewById(R.id.save_supplement_btnID);


        saveSuppelement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSupplement();
            }
        });


    }

    private void saveSupplement() {
        String supplement_Name = supplementName.getText().toString();
        String supplement_des = supplementDes.getText().toString();

        if (supplement_Name == null || supplement_Name.isEmpty()) {
            supplementName.setError("Supplement Name Required");
            return;
        }

        SupplementPost post = new SupplementPost();
        DocumentReference userDoc = db.collection("users").document(userID);

        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String first = documentSnapshot.getString("firstName");

                if (first != null) {
                    // Set username and other fields inside the callback
                    post.setUsername(first);
                    post.setName(supplement_Name);
                    post.setDes(supplement_des);
                    post.setTimestamp(Timestamp.now());

                    // Now that you have all the data, save it to the database
                    saveSupplementdb(post);
                } else {
                    // Handle the case where the username is null
                    Log.e("Error", "Username is null");
                }
            }
        });
    }



    void saveSupplementdb(SupplementPost post)
    {
        DocumentReference supplementDoc = db.collection("Supplements").document();
        supplementDoc.set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    Toast.makeText(AddSupplementsActivity.this, "Supplement saved successfully", Toast.LENGTH_SHORT).show();



                }else {
                    Toast.makeText(AddSupplementsActivity.this, "Supplement saved FAILED", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }






}