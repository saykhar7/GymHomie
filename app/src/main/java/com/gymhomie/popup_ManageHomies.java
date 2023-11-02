package com.gymhomie;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class popup_ManageHomies extends Activity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String userPath = "users"; //path for all the users
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_manage_homies);
        DisplayMetrics dm = new DisplayMetrics();
        Button addHomieBtn =  findViewById(R.id.addHomieButton);
        TextView resultMessage = findViewById(R.id.homieSearchResult);
        TextInputEditText homiesEmailText = findViewById(R.id.emailEditText);
        String homiesEmail = homiesEmailText.getText().toString();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width *.9),(int)(height*.7));
        addHomieBtn.setOnClickListener(new View.OnClickListener() { // This will search user's to match one with the email
            @Override
            public void onClick(View view) {
                db.collection(userPath).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    String currentEmail = document.getString("email");
                                    if(currentEmail == homiesEmail){//compares textfield to document's email
                                        //Once the correct user is identified
                                    }
                                }
                            }
                        });
            }
        });
    }
}
