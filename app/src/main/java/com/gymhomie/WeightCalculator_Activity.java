package com.gymhomie;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeightCalculator_Activity extends AppCompatActivity {
        private Button weightCalc_submitBtn;
        private TextInputEditText targetText;
        private TextView outputText;
        private double desiredTarget;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_weightcalculator);

            weightCalc_submitBtn = findViewById(R.id.weightcalc_submit);
            targetText = findViewById(R.id.weightDesired);
            targetText.setInputType(InputType.TYPE_CLASS_NUMBER);

            outputText = findViewById(R.id.weightCalculatorOutputText);

            weightCalc_submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double t = Double.parseDouble(String.valueOf(targetText.getText()));
                    String s = calculatePlatesNeeded(t);
                    outputText.setText(s);
                }
            });
        }
    public String calculatePlatesNeeded(double desiredTarget){
        //Plate Weight , Amount
        this.desiredTarget = desiredTarget;
        Map<Double, Integer> neededPlates = new HashMap<>();
        String textOutput ="";
        if (desiredTarget > 45) {
            this.desiredTarget -= 45;
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 45);
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 35);
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 25);
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 10);
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 5);
            textOutput= traversingThroughWeights(textOutput,this.desiredTarget, 2.5);

        }

        double totalOnBar =0;

        totalOnBar += 45;

        return textOutput;
    }
    public double updateTarget(double target, double plateValue){
        double  newTarget =  (target) - ((target / (plateValue * 2)));
        return newTarget;
    }
    public String traversingThroughWeights(String current, double target, double plateValue){
            int neededPlates = (int) target / ((int)plateValue * 2);
            double newTarget = (int) target % ((int)plateValue * 2);
            this.desiredTarget = newTarget;
            current += "You will need("+neededPlates+") pair(s) of "+plateValue+"'s lb plate \n";
            return current;
    }
}
