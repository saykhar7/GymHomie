package com.gymhomie.gymMembership;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gymhomie.R;

public class GymMembership extends AppCompatActivity {


    Button qrbutton;

    public static TextView qrtext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_membership);


        qrbutton = findViewById(R.id.scanQrButtonID);

        qrtext = findViewById(R.id.qrDataID);

        qrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Qrscanner.class));

            }
        });



    }
}