package com.gymhomie.events_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import com.gymhomie.MainActivity;
import com.gymhomie.R;
import com.gymhomie.events_finder.client.APICleint;
import com.gymhomie.events_finder.client.APInterface;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsLayoutActivity extends AppCompatActivity implements LocationListener{



    RecyclerView events_list;
    EventsListAdapter eventsListAdapter;

    //After API Fetch
    ArrayList<EventsList> eventsLists;
    private TextView zipcode;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.events_layout);


        //initializing recycleview for events
        eventsLists = new ArrayList<>();

        events_list = findViewById(R.id.eventsRecycleViewID);
        events_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        eventsListAdapter = new EventsListAdapter(EventsLayoutActivity.this, eventsLists);
        events_list.setAdapter(eventsListAdapter);
        populateServices();





        grantPermission();

        checklocationstatus();

        getLocation();

        zipcode = findViewById(R.id.currentzipcodeID);


    }

    public void populateServices(){
        APICleint.getClient().create(APInterface.class).geteventsList().enqueue(new Callback<EventsListApiResponse>() {
            @Override
            public void onResponse(Call<EventsListApiResponse> call, Response<EventsListApiResponse> response) {
                if (response.code()==200){
                    eventsLists.addAll(response.body().getData());
                    eventsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<EventsListApiResponse> call, Throwable t) {

            }
        });
    }


    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);

        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }
    private void checklocationstatus() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try{
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(EventsLayoutActivity.this)
                    .setCancelable(false)
                    .setTitle("Enable GPS Service")
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //this will redirect user to the locaiton setting if location is not enabled
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("cancel", null).show();
        }
    }

    private void grantPermission() {


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            &&ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }



    }



    @Override
    public void onLocationChanged(@NonNull Location location) {



        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

            zipcode.setText(address.get(0).getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}