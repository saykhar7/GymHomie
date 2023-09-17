package com.gymhomie;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class CalculatorMenu_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatormenu);
        Button weightButton = findViewById(R.id.WeightCalculatorButton);
        Button barbButton = findViewById(R.id. barbellCalculatorButton);

        barbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculatorMenu_Activity.this, BarbellCalculator_Activity.class);
                startActivity(intent);
            }
        });
        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculatorMenu_Activity.this, WeightCalculator_Activity.class);
                startActivity(intent);
            }
        });
    }

}