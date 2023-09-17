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
    private double total = 45;



    private double fortyFive = 45;
    private double thirtyFive = 35;
    private double twentyFive = 25;
    private double ten = 10;
    private double five = 5;
    private double twoPointFive = 2.5;

    private TextView countTextFortyFive;
    private TextView countTextThirtyFive;
    private TextView countTextTwentyFive;
    private TextView countTextTen;
    private TextView countTextFive;
    private TextView countTextTwoPointFive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barbellcalculator);
        plusbtn45 = findViewById(R.id.plus45);
        plusbtn35 = findViewById(R.id.plus35);
        plusbtn25 = findViewById(R.id.plus25);
        plusbtn10 = findViewById(R.id.plus10);
        plusbtn5 = findViewById(R.id.plus5);
        plusbtn2_5 = findViewById(R.id.plus2_5);
        minusbtn45 = findViewById(R.id.minus45);
        minusbtn35 = findViewById(R.id.minus35);
        minusbtn25 = findViewById(R.id.minus25);
        minusbtn10 = findViewById(R.id.minus10);
        minusbtn5 = findViewById(R.id.minus5);
        minusbtn2_5 = findViewById(R.id.minus2_5);
        totalText = findViewById(R.id.totalCalcText);

        totalText.setText("45 Lbs");
        countTextFortyFive = findViewById(R.id.countFortyFive);
        countTextThirtyFive = findViewById(R.id.countThirtyFive);
        countTextTwentyFive = findViewById(R.id.countTwentyFive);
        countTextTen = findViewById(R.id.countTen);
        countTextFive = findViewById(R.id.countFive);
        countTextTwoPointFive = findViewById(R.id.countTwoPointFive);


        plusbtn45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               add(fortyFive);
               addCount(countTextFortyFive);
            }
        });
        plusbtn35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(thirtyFive);
                addCount(countTextThirtyFive);
            }
        });
        plusbtn25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(twentyFive);
                addCount(countTextTwentyFive);
            }
        });
        plusbtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(ten);
                addCount(countTextTen);
            }
        });
        plusbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(five);
                addCount(countTextFive);
            }
        });
        plusbtn2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(twoPointFive);
                addCount(countTextTwoPointFive);
            }
        });
        minusbtn45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(fortyFive);
                subCount(countTextFortyFive);
            }
        });
        minusbtn35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(thirtyFive);
                subCount(countTextThirtyFive);
            }
        });
        minusbtn25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(twentyFive);
                subCount(countTextTwentyFive);
            }
        });
        minusbtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(ten);
                subCount(countTextTen);
            }
        });
        minusbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(five);
                subCount(countTextFive);
            }
        });
        minusbtn2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub(twoPointFive);
                subCount(countTextTwoPointFive);
            }
        });
    }

    public void add(double val){
        val = val * 2;
        total += val;
        totalText.setText(Double.toString(total)+ "lbs");
    }
    public void sub(double val){
        val = val * 2;
        total -= val;
        totalText.setText(Double.toString(total)+ "lbs");
    }
    public void addCount(TextView current){
        String currentCountString = current.getText().toString();
        int currentCount = Integer.parseInt(currentCountString);
        currentCount += 2;
        current.setText(Integer.toString(currentCount));
    }
    public void subCount(TextView current ){
        String currentCountString = current.getText().toString();
        int currentCount = Integer.parseInt(currentCountString);
        currentCount -= 2;
        current.setText(Integer.toString(currentCount));
    }
}

