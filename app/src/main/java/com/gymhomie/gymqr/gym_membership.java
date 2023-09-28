package com.gymhomie.gymqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

public class gym_membership extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/"+userID+"/GymMembership"; //path for the water intakes on firestore


    private TextView gym_membership_qr;
    private Button save_membership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_membership_view);

        gym_membership_qr = findViewById(R.id.qrDataID);

        save_membership = findViewById(R.id.scanQrButtonID);

        save_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(gym_membership.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan Bar/QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.initiateScan();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult!=null)
        {
            String contents = intentResult.getContents();
            if(contents!=null)
            {
                saveToDb(contents);

            }
            else
            {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    private void saveToDb(String contents) {

        Map<String, Object> qrData = new HashMap<>();
        qrData.put("qr_contents", contents);


        db.collection(collectionPath).document("QrData").set(qrData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(gym_membership.this, "Qr Data Saved to DB", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(gym_membership.this, "Somewhing Went Wrong", Toast.LENGTH_SHORT).show();

                    }
                }
        );



    }
}

