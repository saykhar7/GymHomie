package com.gymhomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GoalMenu_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalmenu);
        Button addGoalsButton = findViewById(R.id.addGoalsButton);
        Button viewGoalsButton = findViewById(R.id.viewGoalsButton);
        Button updateGoalsButton = findViewById(R.id.updateGoalsButton);
        Button deleteGoalsButton = findViewById(R.id.deleteGoalsButton);

        addGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalMenu_Activity.this, Goal_Activity.class);
                startActivity(intent);
            }
        });
        viewGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalMenu_Activity.this, Goal_Activity.class);
                startActivity(intent);
            }
        });
        updateGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalMenu_Activity.this, Goal_Activity.class);
                startActivity(intent);
            }
        });
        deleteGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalMenu_Activity.this, Goal_Activity.class);
                startActivity(intent);
            }
        });
    }
}