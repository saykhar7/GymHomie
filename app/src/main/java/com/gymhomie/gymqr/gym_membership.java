package com.gymhomie.gymqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;

public class gym_membership extends AppCompatActivity {

    // Obtain an instance of FirebaseAuth for user authentication
    static FirebaseAuth auth = FirebaseAuth.getInstance();

    // Obtain an instance of FirebaseFirestore for interacting with Firestore database
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get the current user's unique identifier (UID)
    static String userID = auth.getCurrentUser().getUid();

    // Define the Firestore collection path based on the user's UID for Gym Membership data
    static final String collectionPath = "users/" + userID + "/GymMembership"; // Path for Gym Membership data in Firestore



    // Declare a TextView for displaying gym membership QR code data
    private TextView gym_membership_qr;

    // Declare a Button for triggering actions related to gym membership
    private Button save_membership;

    // Declare an ImageView for displaying the gym membership QR code image
    private ImageView membershipQR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_membership_view);

        gym_membership_qr = findViewById(R.id.qrDataID);

        save_membership = findViewById(R.id.scanQrButtonID);

        membershipQR = findViewById(R.id.qrcontentsID);





        // Set an OnClickListener for the 'save_membership' Button to handle click events

        save_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an IntentIntegrator instance for QR code scanning
                IntentIntegrator intentIntegrator = new IntentIntegrator(gym_membership.this);

                // Allow screen orientation to change during scanning
                intentIntegrator.setOrientationLocked(false);


                intentIntegrator.setPrompt("Scan Bar/QR Code");


                // Specify desired barcode formats for scanning (All code types in this case
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

                // Initiate the QR code scanning process
                intentIntegrator.initiateScan();


            }
        });

        retrieveContent(listener);

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


    private static void retrieveContent(OnContentReceivedListener listener) {

        DocumentReference dbRef = db.collection(collectionPath).document("QrData");
        dbRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Handle the error here if needed
                listener.onContentReceived("Error occurred");
                return;
            }

            String content = "No Qr/Barcode data";
            if (value != null && value.exists()) {
                content = value.getString("qr_contents");
            }
            listener.onContentReceived(content);
        });
    }

    // Define an interface for the callback
    interface OnContentReceivedListener {
        void onContentReceived(String content);
    }




    // Create a new OnContentReceivedListener implementation
    OnContentReceivedListener listener = new OnContentReceivedListener() {
        @Override
        public void onContentReceived(String content) {
// Set the text of the TextView to the content
            gym_membership_qr.setText(content);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.CODE_128, membershipQR.getWidth(), membershipQR.getHeight());
                Bitmap bitmap = Bitmap.createBitmap(membershipQR.getWidth(), membershipQR.getHeight(), Bitmap.Config.RGB_565);
                for (int i=0; i<membershipQR.getWidth();i++)
                {
                    for(int j=0; j<membershipQR.getHeight(); j++)
                    {
                        bitmap.setPixel(i,j, bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                    }
                }
                membershipQR.setImageBitmap(bitmap);

            } catch (WriterException e) {
                throw new RuntimeException(e);
            }


        }
    };



}

