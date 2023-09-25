package com.gymhomie;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import  androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Calculator_Activity extends AppCompatActivity {
    private Button weightCalcButton;
    private Button caloriesCalcButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        weightCalcButton = findViewById(R.id.weightCalcButton);
        caloriesCalcButton = findViewById(R.id.caloriesCalcButton);
        weightCalcButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start the Tool_Activity
                Intent intent = new Intent(Calculator_Activity.this, BarbellCalculator_Activity.class);
                //Intent intent = new Intent(getActivity(), Calculator_Activity.class);
                startActivity(intent);
            }
        });
        caloriesCalcButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start the Tool_Activity
                Intent intent = new Intent(Calculator_Activity.this, CaloriesCalculator_Activity.class);
                //Intent intent = new Intent(getActivity(), Calculator_Activity.class);
                startActivity(intent);
            }
        });
    }
}
