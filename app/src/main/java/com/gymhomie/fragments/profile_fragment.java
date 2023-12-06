package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.Achievement_Activity;
import com.gymhomie.Body_Measurement_Activity;
import com.gymhomie.Goal_Activity;
import com.gymhomie.R;

import java.util.HashMap;
import java.util.Map;


public class profile_fragment extends Fragment {


    private Button btnLogout;
    private Button btnGoals;
    private Button btnAchievements;
    private Button btnBodyMeasurement;
    private ImageView profileBadge;
    private TextView profileName;
    private TextView profileEmail;
    private OnLogoutClickListener onLogoutClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    private static final int achReqCode = 100;
    private View view;

    public profile_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference userDoc = db.collection("users").document(userID);
        userDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String first = (documentSnapshot.get("firstName").toString());
                        String last = (documentSnapshot.get("lastName").toString());
                        profileName.setText(first+ " " + last);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Profile Fragment", "No user doc info found");
                        profileName.setText("DNE");
                    }
                });
        String email = auth.getCurrentUser().getEmail();
        profileEmail.setText(email);

        profileBadge = view.findViewById(R.id.profileBadge);
        btnLogout = view.findViewById(R.id.logoutBtn);
        btnGoals = view.findViewById(R.id.goalsBtn);

        updateBadge(view);

        btnGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Goal_Activity.class);
                startActivity(intent);
            }
        });
        btnAchievements = view.findViewById(R.id.achievementsButton);

        btnLogout.setOnClickListener(v -> {
            if (onLogoutClickListener != null) {
                onLogoutClickListener.onLogout();
            }
        });
        btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the achievements activity
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });


        btnBodyMeasurement = view.findViewById(R.id.body_measurement_button);
        btnBodyMeasurement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Body_Measurement_Activity.class);
                startActivity(intent);
            }
        });

        profileBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the achievements so the user can pick their favorite badge
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void updateBadge(View view) {
        // let's update the badge icon on the users profile

        Map<String, Integer> imageResourceMap = new HashMap<>();
        imageResourceMap.put("1", R.drawable.achievement_1_unlocked);
        imageResourceMap.put("2", R.drawable.achievement_2_unlocked);
        imageResourceMap.put("3", R.drawable.achievement_3_unlocked);
        imageResourceMap.put("4", R.drawable.achievement_4_unlocked);

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String achID;
                        if (!documentSnapshot.contains("badge")) {
                            // badge has not been set for current user, set to -1 for default
                            achID = "-1";
                            docRef.update("badge", achID);
                            profileBadge.setImageResource(R.drawable.trophy_unlocked);
                        }
                        else {
                            achID = documentSnapshot.get("badge").toString();
                            if (achID.equals("-1")) {
                                profileBadge.setImageResource(R.drawable.trophy_unlocked);
                            }
                            else {
                                // dynamically set their badge
                                Log.d("Profile Achievement Update", achID);
                                profileBadge.setImageResource(imageResourceMap.get(achID));
                            }
                        }
                    }
                });


    }

    public void setOnLogoutClickListener(OnLogoutClickListener listener) {
        this.onLogoutClickListener = listener;
    }

    public interface OnLogoutClickListener {
        void onLogout();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBadge(view);
    }
}