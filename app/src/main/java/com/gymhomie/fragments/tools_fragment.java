package com.gymhomie.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gymhomie.R;


public class tools_fragment extends Fragment {
    private Button barbell_Calculator;
    private profile_fragment.OnLogoutClickListener onLogoutClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        barbell_Calculator = (view.findViewById(R.id.barcalcBtn));
        if (barbell_Calculator != null) {
            barbell_Calculator.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {
                    setContentView(R.layout.activity_login);
                }
            }));

        return inflater.inflate(R.layout.fragment_tools, container, false);

    }




}