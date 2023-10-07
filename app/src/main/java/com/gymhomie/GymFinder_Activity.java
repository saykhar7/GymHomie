package com.gymhomie;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


public class GymFinder_Activity extends AppCompatActivity {

    // Inside your activity or fragment
    private FusedLocationProviderClient fusedLocationClient;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button openInMapsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfinder);

        // Initialize the fusedLocationClient in your onCreate method
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ImageView magnifyingGlassImageView = findViewById(R.id.magnifyingGlassImageView);

        magnifyingGlassImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check and request location permission
                if (checkLocationPermission()) {
                    // Request the user's last known location
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(GymFinder_Activity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        // Now you have the user's latitude and longitude
                                        // You can use these values as needed
                                        // After obtaining the location, you can open maps or perform other actions
                                        openMaps(latitude, longitude);
                                    }
                                }
                            });
                }
            }
        });
    }

    // Define a method to open maps with the obtained latitude and longitude
    private void openMaps(double latitude, double longitude) {
        String uri = "geo:" + latitude + "," + longitude + "?q=" + Uri.encode("Nearest gyms");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(mapIntent);
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

}
