package com.gymhomie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BMI_Activity extends AppCompatActivity {

    private EditText weight;
    private NumberPicker feet;
    private NumberPicker inches;
    private TextView user_bmi;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        weight = findViewById(R.id.weight_input);
        feet = findViewById(R.id.feet_input);
        inches = findViewById(R.id.inches_input);
        Button calculate_button = findViewById(R.id.calculate_button);
        user_bmi = findViewById(R.id.user_BMI);

        feet.setMinValue(1);
        feet.setMaxValue(8);
        feet.setValue(5);

        inches.setMinValue(0);
        inches.setMaxValue(11);
        inches.setValue(0);

        calculate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBMI();
            }
        });

    }
    public void calculateBMI(){
        int myWeight = Integer.parseInt(weight.getText().toString());
        int myFeet = feet.getValue();
        int myInches = inches.getValue();
        int myHeight = (myFeet * 12) + myInches;
        int bmi = 703 * myWeight / (myHeight*myHeight);

        user_bmi.setText(Integer.toString(bmi));
    }
}
