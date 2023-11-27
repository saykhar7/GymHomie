package com.gymhomie.tools;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Homie {
    public Homie(){
        paths = new ArrayList<>();
        homie = new ArrayList<>();
        setPaths(paths);

    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String userPath = "users";
    ArrayList<String> homie;
    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> homiePath) {
        String userID = auth.getCurrentUser().getUid();
        String thisUserPathtoHomies = "users/" + userID + "/Homies";
        db.collection(thisUserPathtoHomies).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference reference = document.getDocumentReference("HomieID");
                            String path = reference.getPath();
                            homiePath.add(path);
                        }
                        for(String path: homiePath){
                            setHomies(path);
                        }
                    }
                });
        this.paths = homiePath;
    }

    ArrayList<String> paths;
    public ArrayList<String> getHomies(){
        return homie;
    }
    public void setHomies(String path){
        String userID = auth.getCurrentUser().getUid();
        String currUserPathtoHomies = path;
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            if(documentSnapshot.getId().equalsIgnoreCase(currUserPathtoHomies.substring(6,currUserPathtoHomies.length()-1)))
                            {
                                String firstName = documentSnapshot.get("firstName").toString();
                                String lastName = documentSnapshot.get("lastName").toString();
                                homie.add(firstName + " " + lastName);
                            }
                        }
                    }
                                });
    }
}
