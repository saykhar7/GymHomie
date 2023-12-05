package com.gymhomie.gymqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String userID = auth.getCurrentUser().getUid();
    static final String collectionPath = "users/" + userID + "/GymMembership";

    private TextView gym_membership_qr;
    private Button save_membership;
    private ImageView membershipQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_membership_view);

        gym_membership_qr = findViewById(R.id.qrDataID);
        save_membership = findViewById(R.id.scanQrButtonID);
        membershipQR = findViewById(R.id.qrcontentsID);

        save_membership.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(gym_membership.this);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setPrompt("Scan Bar/QR Code");
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.initiateScan();
        });

        retrieveContent(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                saveToDb(contents);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveToDb(String contents) {
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("qr_contents", contents);

        db.collection(collectionPath).document("QrData").set(qrData)
                .addOnSuccessListener(unused ->
                        Toast.makeText(gym_membership.this, "Qr Data Saved to DB", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(gym_membership.this, "Something Went Wrong", Toast.LENGTH_SHORT).show());
    }

    private void generateBarcode(String content) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        int width = membershipQR.getWidth();
        int height = membershipQR.getHeight();

        // Create a default bitmap if dimensions are not valid
        if (width <= 0 || height <= 0) {
            width = 800; // Set a default width
            height = 400; // Set a default height
        }

        try {
            BarcodeFormat format = BarcodeFormat.CODE_128;
            BitMatrix bitMatrix = multiFormatWriter.encode(content, format, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            membershipQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }



    private static void retrieveContent(OnContentReceivedListener listener) {
        DocumentReference dbRef = db.collection(collectionPath).document("QrData");
        dbRef.addSnapshotListener((value, error) -> {
            if (error != null) {
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

    interface OnContentReceivedListener {
        void onContentReceived(String content);
    }

    OnContentReceivedListener listener = content -> {
        gym_membership_qr.setText(content);
        generateBarcode(content);
    };

    @Override
    protected void onResume() {
        super.onResume();
        retrieveContent(listener);
    }


}


