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
import com.gymhomie.popup_ManageHomies;

public class homie_fragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userID = auth.getCurrentUser().getUid();
    String collectionPath = "users/" + userID + "/homies"; //path for the water intakes on firestore

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homie, container, false);
        TextView homie_title = view.findViewById(R.id.homies_title);
        ConstraintLayout constraintLayout = view.findViewById(R.id.homie_constraintlayout);
        Button mangeHomiesBtn = (Button) view.findViewById(R.id.homie_request_btn);
        mangeHomiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), popup_ManageHomies.class));
            }
        });
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference homiesDocument = (DocumentReference) document.get("homie");
                            homiesDocument.get().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    DocumentSnapshot names = task.getResult();
                                            int initialMargin = 40;

                                                TextView textView = new TextView(view.getContext());
                                                textView.setText(names.getString("firstName")+ " "+names.getString("lastName"));
//                                                ConstraintSet constraintSet = new ConstraintSet();
//                                                constraintSet.clone(constraintLayout);
//                                               constraintSet.centerHorizontally(textView.getId(), ConstraintSet.PARENT_ID);
//                                                constraintSet.constrainHeight(textView.getId(), 30);
//                                                constraintSet.constrainWidth(textView.getId(), 100);
//                                                constraintSet.connect(textView.getId(), ConstraintSet.TOP, homie_title.getId(), ConstraintSet.BOTTOM, initialMargin);
                                                initialMargin += 40;

                                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                                    lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                                    lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                                    lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

                                    textView.setTextSize(16); // Set text size in SP
                                    textView.setTextColor(Color.BLACK); // Set text color
                                    textView.setBackgroundColor(Color.GRAY); // Set background color
                                    textView.setLayoutParams(lp);
                                    constraintLayout.addView(textView);
                                  //  constraintSet.applyTo(constraintLayout);
                                    initialMargin += 40;
                                        }
                                    });
                        }
                    }

                });


        return view;
    }
}