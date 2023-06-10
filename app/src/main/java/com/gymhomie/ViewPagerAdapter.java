package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gymhomie.fragments.home_fragment;
import com.gymhomie.fragments.homie_fragment;
import com.gymhomie.fragments.profile_fragment;
import com.gymhomie.fragments.tools_fragment;
import com.gymhomie.fragments.workout_fragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            //here we are retuing each class for each selection on the navigation
            // where home() is default

            case 0 : return new home_fragment();
            case 1: return new workout_fragment();
            case 2: return new homie_fragment();
            case 3: return new tools_fragment();
            case 4:return new profile_fragment();
            default:return new home_fragment();

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
