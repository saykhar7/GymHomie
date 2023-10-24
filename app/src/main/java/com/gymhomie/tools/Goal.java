package com.gymhomie.tools;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Goal {
    // Initiate Firestore instance and authorization
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String user_id;
    public Goal(){
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getUid();
    }
    public boolean hasGoals(){
        return true;
    }
    public String[] getCollections(){
        String[] collections = new String[5];
        DocumentReference dc = firestore.document("users/" + user_id);
        //dc.
        return collections;
    }
}
