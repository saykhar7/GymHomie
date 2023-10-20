package com.gymhomie;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.tools.GymReminder;

import java.util.HashMap;
import java.util.Map;

public class Achievement_Activity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String baseAchievementsPath = "achievements";
    String userAchievementsPath = "users/" + userID + "/Achievements";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_achievements);
        updateMissingAchievements();

    }

    private void updateMissingAchievements() {
        // to add achievements to the user's own achievements collection
        // For example, imagine a new user...we need to have the documents
        // for each achievement stored before we can track and display them
        // Probably move to login later
        // (so they don't have to open achievements to unlock the feature)

        // first we need to grab all possible achievements (imagine we have an update)
        db.collection(baseAchievementsPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryBaseDocumentSnapshots) {
                        // quick check for length...if equal they have all of them
                        db.collection(userAchievementsPath).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryUserDocumentSnapshots) {
                                        if (queryUserDocumentSnapshots.size() != queryBaseDocumentSnapshots.size()) {
                                            // sizes are different, must update

                                            for (DocumentSnapshot baseAchievement : queryBaseDocumentSnapshots.getDocuments()) {
                                                //with each base ach, let's grab each field
                                                // criteria(number) description(string) id(number) name(string)
                                                String baseAchID = (String) baseAchievement.get("id");
                                                int criteria = ((Long) baseAchievement.get("criteria")).intValue();
                                                String description = (String) baseAchievement.get("description");
                                                String name = (String) baseAchievement.get("name");
                                                DocumentReference userDocRef = db.collection(userAchievementsPath).document(baseAchID);
                                                userDocRef.get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshotAttempt) {
                                                                if (!documentSnapshotAttempt.exists()) {
                                                                    // we did not find the corresponding achievement in the user's collection -> add it
                                                                    Map<String, Object> newDoc = new HashMap<>();
                                                                    newDoc.put("name", name);
                                                                    newDoc.put("description", description);
                                                                    newDoc.put("criteria", criteria);
                                                                    newDoc.put("progress", 0);
                                                                    newDoc.put("unlocked", false);
                                                                    db.collection(userAchievementsPath).document(baseAchID).set(newDoc);
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // TODO : handle db error
                                                                Log.e("Achievement Existing Retrieval", "Failure to retrieve existing doc");
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("Achievement Existing Retrieval", "All Docs already exist");
                                        }
                                    }
                                });
                    }
                });
    }
}