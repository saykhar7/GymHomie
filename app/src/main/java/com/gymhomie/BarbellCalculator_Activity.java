package com.gymhomie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class BarbellCalculator_Activity extends AppCompatActivity {
    //Plus Buttons
    private Button plusbtn45;
    private Button plusbtn35;
    private Button plusbtn25;
    private Button plusbtn10;
    private Button plusbtn5;
    private Button plusbtn2_5;

    //Negative Buttons
    private Button minusbtn45;
    private Button minusbtn35;
    private Button minusbtn25;
    private Button minusbtn10;
    private Button minusbtn5;
    private Button minusbtn2_5;

    //textView
    private TextView totalText;

    //
    private double total;



    private double fortyFive = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        double total = 0;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barbellcalculator);
        plusbtn5 = findViewById(R.id.plus45);
        totalText = findViewById(R.id.totalCalcText);
        plusbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               add(fortyFive);
            }
        });
    }

    public void add(double val){
        total += val;
        totalText.setText(Double.toString(total)+ "lbs");
    }
    public void sub(double val){
        total -= val;
    }
}

