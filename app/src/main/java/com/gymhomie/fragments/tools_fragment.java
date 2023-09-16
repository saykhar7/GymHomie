package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gymhomie.BarbellCalculator_Activity;
import com.gymhomie.R;
import com.gymhomie.Step_Activity;
import com.gymhomie.Water_Intake_Activity;


public class tools_fragment extends Fragment {
    private Button barbell_Calculator;
    private Button step_counter_button;

    private Button water_intake_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        barbell_Calculator = view.findViewById(R.id.calculator_button);
        step_counter_button = view.findViewById(R.id.step_counter_button);
        water_intake_button = view.findViewById(R.id.water_intake_button);

        barbell_Calculator.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start the Tool_Activity
                Intent intent = new Intent(getActivity(), BarbellCalculator_Activity.class);
                startActivity(intent);
            }
        });
        // Set a click listener for step_counter_button
        step_counter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Tool_Activity
                Intent intent = new Intent(getActivity(), Step_Activity.class);
                startActivity(intent);
            }
        });

        water_intake_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Water_Intake_Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}