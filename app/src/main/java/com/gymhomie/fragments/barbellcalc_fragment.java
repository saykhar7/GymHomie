package com.gymhomie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gymhomie.NameViewModel;
import com.gymhomie.R;


public class barbellcalc_fragment extends Fragment {
    private Button barbell_Calculator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        barbell_Calculator = (view.findViewById(R.id.barcalcBtn));


        return inflater.inflate(R.layout.fragment_tools, container, false);
    }
}