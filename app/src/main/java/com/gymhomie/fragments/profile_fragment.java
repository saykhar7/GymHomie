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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gymhomie.Achievement_Activity;
import com.gymhomie.GoalMenu_Activity;
import com.gymhomie.R;


public class profile_fragment extends Fragment {


    private Button btnLogout;
    private Button btnGoals;
    private Button btnAchievements;
    private ImageView pickAchievement;
    private TextView profileName;
    private TextView profileEmail;
    private OnLogoutClickListener onLogoutClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();

    public profile_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        pickAchievement = view.findViewById(R.id.profileBadge);
        btnLogout = view.findViewById(R.id.logoutBtn);
        btnGoals = view.findViewById(R.id.goalsBtn);

        btnGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoalMenu_Activity.class);
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
        pickAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the achievements so the user can pick their favorite badge
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void setOnLogoutClickListener(OnLogoutClickListener listener) {
        this.onLogoutClickListener = listener;
    }

    public interface OnLogoutClickListener {
        void onLogout();
    }
}