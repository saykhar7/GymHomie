package com.gymhomie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gymhomie.Achievement_Activity;
import com.gymhomie.R;


public class profile_fragment extends Fragment {


    private Button btnLogout;
    private Button btnAchievements;
    private OnLogoutClickListener onLogoutClickListener;

    public profile_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = view.findViewById(R.id.logoutBtn);
        btnAchievements = view.findViewById(R.id.achievementsButton);

        btnLogout.setOnClickListener(v -> {
            if (onLogoutClickListener != null) {
                onLogoutClickListener.onLogout();
            }
        });
        btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the achievements activity
                Intent intent = new Intent(getActivity(), Achievement_Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void setOnLogoutClickListener(OnLogoutClickListener listener) {
        this.onLogoutClickListener = listener;
    }

    public interface OnLogoutClickListener {
        void onLogout();
    }
}