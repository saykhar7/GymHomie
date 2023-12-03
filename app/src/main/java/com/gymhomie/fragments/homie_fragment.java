package com.gymhomie.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.R;
import com.gymhomie.popup_ListHomies;
import com.gymhomie.popup_ManageHomies;

public class homie_fragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/" + userID + "/Homies"; //path for the water intakes on firestore

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homie, container, false);
        TextView homie_title = view.findViewById(R.id.homies_title);
        ConstraintLayout constraintLayout = view.findViewById(R.id.homie_constraintlayout);
        Button mangeHomiesBtn = (Button) view.findViewById(R.id.homie_request_btn);
        Button homieListBtn = (Button) view.findViewById(R.id.homie_list_btn);
        mangeHomiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), popup_ManageHomies.class));
            }
        });

        homieListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), popup_ListHomies.class));
            }
        });
       return view;
    }
}