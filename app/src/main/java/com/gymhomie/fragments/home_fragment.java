package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.gymhomie.NameViewModel;
import com.gymhomie.R;
import com.gymhomie.events_finder.EventsLayoutActivity;
import com.gymhomie.gymqr.gym_membership;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class home_fragment extends Fragment {


    private NameViewModel nameViewModel;
    private TextView fullName;

    private FrameLayout access_gym_membership;

    private FrameLayout events_finder;

    public interface HomeController
    {
        void onFirstNameLastNameFetched(String firstName, String lastName);
    }

    public home_fragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fullName = view.findViewById(R.id.fullNameText);


        //button to open events finder page near your location

        events_finder = view.findViewById(R.id.events_ID);

        //Listener for events button
        events_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EventsLayoutActivity.class);
                startActivity(i);
            }
        });

        access_gym_membership = view.findViewById(R.id.open_membershipID);
        access_gym_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFirestoreDocumentUpdates();
                Intent i = new Intent(getActivity(), gym_membership.class);
                startActivity(i);
            }
        });



        nameViewModel = new ViewModelProvider(requireActivity()).get(NameViewModel.class);
        nameViewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String fullNameText) {
                fullName.setText(fullNameText);
            }
        });

        return view;
    }

    private void handleFirestoreDocumentUpdates() {
        // Use 'scannedData' to identify the document path or other necessary information
        // Construct a Firestore reference to the document

        // Check if the 'streak' variable of type map exists in the document
        // If it exists, update the value at the key "count" to the current value plus one
        // If it doesn't exist, create and initialize the 'streak' map with {"count": 1}

        // Example code for Firestore update (make sure to replace with your Firestore logic)
        DocumentReference documentRef = FirebaseFirestore.getInstance().document("users/o5Jq1RqXryOUZhF9ccWxuByuiZC2/GymMembership/QrData");
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null && data.containsKey("streak") && data.get("streak") instanceof Map) {
                        // The 'streak' map exists
                        Map<String, Object> streakMap = (Map<String, Object>) data.get("streak");
                        // Get the current date
                        Date currentDate = new Date();
                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
                        // Format the current date as a string
                        String formattedDate = dateFormat.format(currentDate);
                        if (streakMap.containsKey(formattedDate)) {
                            long currentCount = (long) streakMap.get(formattedDate);
                            // Update the count
                            streakMap.put(formattedDate, currentCount + 1);
                        } else {
                            // Initialize 'count' to 1
                            streakMap.put(formattedDate, 1);
                        }
                        // Update the document with the modified 'streak' map
                        documentRef.set(data, SetOptions.merge());
                    } else {
                        // The 'streak' map doesn't exist; create and initialize it
                        Map<String, Object> streakMap = new HashMap<>();
                        // Get the current date
                        Date currentDate = new Date();
                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
                        String formattedDate = dateFormat.format(currentDate);
                        streakMap.put(formattedDate, 1);
                        data.put("streak", streakMap);
                        // Update the document with the new 'streak' map
                        documentRef.set(data, SetOptions.merge());
                    }
                }
            }
        });
    }
    public void updateName(String firstName, String lastName)
    {
        if(firstName != null && lastName != null)
        {
            fullName.setText("Welcome Back\n"+firstName+ " "+ lastName+" !");
        }
    }
}