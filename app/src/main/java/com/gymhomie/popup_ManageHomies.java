package com.gymhomie;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

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


        String userID = auth.getCurrentUser().getUid();


        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width *.9),(int)(height*.7));
        addHomieBtn.setOnClickListener(new View.OnClickListener() { // This will search user's to match one with the email
            @Override
            public void onClick(View view) {
                String homiesEmail = homiesEmailText.getText().toString();
                db.collection(userPath).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    String currentEmail = document.getString("email");
                                    if(currentEmail.equals(homiesEmail)){//compares textfield to document's email
                                        String thisUserPathtoHomies = "users/"+userID+"/Homies";; //This is the path to the person's searchings collection
                                        String homiesUserPathtoHomies = document.getReference().getPath()+"/Homies";//This is t he path to the person that is being searched
                                        CollectionReference collectionReferenceUser  = db.collection(thisUserPathtoHomies);

                                        CollectionReference collectionReferenceHomie = db.collection(homiesUserPathtoHomies);
                                        //for both these methods we pass the collection reference of the intended target and the docRef for the one being attached so
                                        //the user is the person sending request
                                        //the receiver is the person who has to accept request.
                                        DocumentReference senderDocRef = db.collection("users").document(userID);

                                        addCollectionToUser(collectionReferenceUser, document);
                                        addCollectionToReceiver(collectionReferenceHomie, senderDocRef);


                                    }
                                }
                            }
                        });
            }
        });
    }
    public void addCollectionToUser(CollectionReference collectionReference, DocumentSnapshot homieDocSnap){
        String userID = auth.getCurrentUser().getUid();
   //     QuerySnapshot collectionSnapshot = collectionReference.get;
        DocumentReference homieDocRef = homieDocSnap.getReference();
        String documentPath = homieDocSnap.getReference().getPath()+"/"; // Path to the document where you want to create a subcollection
        String subcollectionName = "Homies";
        String fullPath = collectionReference.getPath();
        //If the collection does not exist the if will  initialize that collection



            DocumentReference documentReference = db.document(documentPath);
            CollectionReference subcollectionReference = documentReference.collection(subcollectionName);
            Map<String, Object> homieCollectionMap = new HashMap<>();


            homieCollectionMap.put("HomieID", homieDocRef);
            homieCollectionMap.put("isHomie", true);
            homieCollectionMap.put("isSender", true);
            subcollectionReference.add(homieCollectionMap);
//            DocumentReference newHomieDocRef = db.collection(fullPath).document();
//            db.collection(fullPath).document().set(homieCollectionMap)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(popup_ManageHomies.this, "Friend request sent", Toast.LENGTH_SHORT).show();
//                    }
//                      })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(popup_ManageHomies.this, "Error Adding Friend!", Toast.LENGTH_SHORT).show();
//                        }
//                    });

    }
    //This method will add into the person who is being sent a request
    public void addCollectionToReceiver(CollectionReference collectionReference, DocumentReference senderDocRef){
        String userID = auth.getCurrentUser().getUid();
        //QuerySnapshot collectionSnapshot = collectionReference.get().getResult();
       // DocumentReference senderDocRef = senderDocSnap.getReference();
        String documentPath = senderDocRef.getPath(); // Path to the document where you want to create a subcollection
        String subcollectionName = "Homies";
        String fullPath = collectionReference.getPath();
        //If the collection does not exist the if will  initialize that collection



        DocumentReference documentReference = db.document(documentPath);
        CollectionReference subcollectionReference = documentReference.collection(subcollectionName);
        Map<String, Object> homieCollectionMap = new HashMap<>();


        homieCollectionMap.put("HomieID", senderDocRef);
        homieCollectionMap.put("isHomie", false);
        homieCollectionMap.put("isSender", false);
        subcollectionReference.add(homieCollectionMap);
        Toast.makeText(popup_ManageHomies.this, "Friend request sent", Toast.LENGTH_SHORT).show();
        //DocumentReference newHomieDocRef = db.collection(fullPath).document();
//        db.collection(fullPath).document().set(homieCollectionMap)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(popup_ManageHomies.this, "Friend request sent", Toast.LENGTH_SHORT).show();
//                    }
//                })
//
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(popup_ManageHomies.this, "Error Adding Friend!", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }
}
