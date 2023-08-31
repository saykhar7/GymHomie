package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gymhomie.R;
import com.gymhomie.toolActivities.WaterIntakeActivity;

public class water_intake_fragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
//        View rootView = inflater.inflate(R.layout.fragment_tools, container, false);
//
//        Button openWaterIntake = rootView.findViewById(R.id.openWaterIntakeButton);
//        openWaterIntake.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), WaterIntakeActivity.class);
//            startActivity(intent);
//        });
//        return rootView;
    }
}
