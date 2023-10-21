//package com.gymhomie.gymqr;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.budiyev.android.codescanner.CodeScanner;
//import com.budiyev.android.codescanner.CodeScannerView;
//import com.budiyev.android.codescanner.DecodeCallback;
//import com.budiyev.android.codescanner.ScanMode;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.Result;
//import com.gymhomie.R;
//import com.gymhomie.Water_Intake_Activity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class QrScanner extends AppCompatActivity {
//
//    private FirebaseFirestore db =a FirebaseFirestore.getInstance();
//
//    FirebaseAuth auth = FirebaseAuth.getInstance();
//    String userID = auth.getCurrentUser().getUid();
//    String collectionPath = "users/"+userID+"/GymMembership"; //path for the water intakes on firestore
//
//    private CodeScanner codeScanner;
//    private CodeScannerView scannerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qr_scanner);
//
//        scannerView = findViewById(R.id.scanner_viewID);
//
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {
//                Manifest.permission.CAMERA
//        };
//
//        if (!hasPermission(this, PERMISSIONS)) {
//
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        }else {
//            runCodeScanner();
//        }
//    }
//
//    public static boolean hasPermission(Context context, String... permissions)
//    {
//        if(context != null && permissions != null)
//        {
//            for(String permission:permissions)
//            {
//                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_DENIED)
//                {
//                    return false;
//                }
//            }
//        }
//
//
//
//        return true;
//    }
//
//    public void runCodeScanner() {
//
//        codeScanner = new CodeScanner(this, scannerView);
//
//        codeScanner.setAutoFocusEnabled(true);
//        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
//        codeScanner.setScanMode(ScanMode.CONTINUOUS);
//        codeScanner.setDecodeCallback(new DecodeCallback() {
//            @Override
//            public void onDecoded(@NonNull Result result) {
//
//                String data = result.getText();
//
//                saveToDb(data);
//
//            }
//        });
//
//    }
//
//    private void saveToDb(String data) {
//
//        Map<String, Object> qrData = new HashMap<>();
//
//
//        db.collection(collectionPath).document("QrData").set(qrData).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(QrScanner.this, "Qr Data Saved to DB", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(
//                new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(QrScanner.this, "Somewhing Went Wrong", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//        );
//
//
//
//    }
//}