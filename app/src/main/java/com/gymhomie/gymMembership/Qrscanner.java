package com.gymhomie.gymMembership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Qrscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    //Instantiating database
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Getting info for currently logged user
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();

    //db path for membership data collected for qr/barcode scanner
    String collectionPath = "users/" + userID+ "GymMembership";


    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result result) {


        String data = result.getText();

        Map<String, Object> qrData = new HashMap<>();

        qrData.put("data", data);


        db.collection(collectionPath).document("qrdata").set(qrData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Qrscanner.this, "Qr Data Saved", Toast.LENGTH_SHORT).show();
                GymMembership.qrtext.setText("I added" + data + "In DB");
                onBackPressed();
            }
        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Qrscanner.this, "Qr Data FAILED to save!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}