package com.gymhomie;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.tools.Achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Achievement_Activity extends AppCompatActivity {
    private ArrayList<Achievement> achievementList;
    private int updatesNeeded;
    private int updatesMade;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String baseAchievementsPath = "achievements";
    String userAchievementsPath = "users/" + userID + "/Achievements";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_achievements);
        updateMissingAchievements(); // also calls viewAchievements when updates are done

    }

    private void viewAchievements() {
        // TODO: BUG -> this method when called at first will not show updates (have to reopen achievements)
        // user can view their achievements and progress
        // called from onAchievementsUpdateComplete within updateMissingAchievements
        // very similar to the gym reminders
        achievementList = new ArrayList<>();
        db.collection(userAchievementsPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String id = (String) documentSnapshot.get("id");
                            int criteria = ((Long) documentSnapshot.get("criteria")).intValue();
                            int progress = ((Long) documentSnapshot.get("progress")).intValue();
                            String description = (String) documentSnapshot.get("description");
                            String name = (String) documentSnapshot.get("name");
                            boolean unlocked = (boolean) documentSnapshot.get("unlocked");

                            Achievement currentAchievement = new Achievement(id, name, description, criteria, progress, unlocked);
                            achievementList.add(currentAchievement);

                        }
                        // update UI
                        RecyclerView recyclerView = findViewById(R.id.achievement_recycler_view);
                        AchievementAdapter adapter = new AchievementAdapter(getApplicationContext(), achievementList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Achievement Viewing Retrieval", "Failure to retrieve user Achievements");
                    }
                });
    }

    private void updateMissingAchievements() {
        // to add achievements to the user's own achievements collection
        // For example, imagine a new user...we need to have the documents
        // for each achievement stored before we can track and display them
        // Probably move to login later
        // (so they don't have to open achievements to unlock the feature)
        // THIS WILL CALL onAchievementsUpdateComplete which calls viewAchievements

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
                                        updatesNeeded = queryBaseDocumentSnapshots.size() - queryUserDocumentSnapshots.size();
                                        updatesMade = 0;
                                        if (updatesNeeded != 0) {
                                            // sizes are different, must update
                                            // TODO : this check does not account for us removing achievements from base
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
                                                                    Log.d("Achievement Viewing Debug", "saving achievement");
                                                                    updatesMade += 1;
                                                                    if (updatesMade == updatesNeeded) {
                                                                        onAchievementUpdateComplete();
                                                                    }

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
                                            Log.d("Achievement Viewing Debug", "end of saving ach for");
                                        } else {
                                            Log.d("Achievement Existing Retrieval", "All Docs already exist");
                                            onAchievementUpdateComplete();
                                        }
                                    }
                                });
                    }
                });
    }
    public void onAchievementUpdateComplete() {
        // so the viewAchievements will wait for the update in db
        Log.d("Achievement Viewing Debug", "cb: now calling view ach");
        viewAchievements();
    }
}