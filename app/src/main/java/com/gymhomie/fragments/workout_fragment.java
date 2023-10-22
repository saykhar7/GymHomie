package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gymhomie.R;
import com.gymhomie.Workout_Activity;


public class workout_fragment extends Fragment {

    Button add_workout_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        add_workout_button = view.findViewById(R.id.add_workout_button);

        add_workout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Workout_Activity.class);
                startActivity(intent);
                //new addWorkout();
            }
        });
        return view;


    }
}