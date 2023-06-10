package com.gymhomie.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.gymhomie.NameViewModel;
import com.gymhomie.R;


public class home_fragment extends Fragment {


    private NameViewModel nameViewModel;
    private TextView fullName;

    public interface HomeController
    {
        void onFirstNameLastNameFetched(String firstName, String lastName);
    }

    public home_fragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fullName = view.findViewById(R.id.fullNameText);

        nameViewModel = new ViewModelProvider(requireActivity()).get(NameViewModel.class);
        nameViewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String fullNameText) {
                fullName.setText(fullNameText);
            }
        });

        return view;
    }


    public void updateName(String firstName, String lastName)
    {
        if(firstName != null && lastName != null)
        {
            fullName.setText("Welcome Back\n"+firstName+ " "+ lastName);
        }
    }
}