package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gymhomie.NameViewModel;
import com.gymhomie.R;
import com.gymhomie.gymqr.gym_membership;


public class home_fragment extends Fragment {


    private NameViewModel nameViewModel;
    private TextView fullName;

    private FrameLayout access_gym_membership;

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


        access_gym_membership = view.findViewById(R.id.open_membershipID);
        access_gym_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), gym_membership.class);
                startActivity(i);


            }
        });



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
            fullName.setText("Welcome Back\n"+firstName+ " "+ lastName+" !");
        }
    }
}