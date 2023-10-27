package com.gymhomie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymhomie.R;

import java.util.ArrayList;

public class Goal_Activity extends AppCompatActivity {

    private ImageView artImageView;
    private TextView title;
    private TextView summary;
    private int currentImageIndex = 0;
    private int[] imageResources = {
            R.drawable.jogging,
            R.drawable.water_bottle,
            R.drawable.body_scan,
            R.drawable.weights,
            R.drawable.stopwatch_icon

    };
    private String[] titleResources = {
            "Step Goals",
            "Hydration Goals",
            "Weight Goals",
            "PR Goals",
            "Check-in Goals"
    };
    private boolean[] hasGoals = new boolean[imageResources.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        artImageView = findViewById(R.id.artImageView);
        title = findViewById(R.id.titleTextView);
        summary = findViewById(R.id.summaryTextView);
        updateArtImageView();
        updateTitle();
        updateSummary();
        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex > 0) {
                    currentImageIndex--;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < imageResources.length - 1) {
                    currentImageIndex++;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });
    }

    private void updateArtImageView() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            artImageView.setImageResource(imageResources[currentImageIndex]);
        }
    }
    private void updateTitle() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            title.setText(titleResources[currentImageIndex]);
        }
    }
    private void updateSummary() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            summary.setText("Active Goals:");
        }
    }

}
