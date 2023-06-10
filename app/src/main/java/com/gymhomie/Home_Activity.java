package com.gymhomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymhomie.fragments.home_fragment;
import com.gymhomie.fragments.profile_fragment;

public class Home_Activity extends AppCompatActivity implements home_fragment.HomeController, profile_fragment.OnLogoutClickListener {

    private NameViewModel nameViewModel;



    ViewPager2 viewPager2;

    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;


    private FirebaseAuth authUser;

    private String userFirstNameinDB,userLastNameinDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_homepage);

        authUser = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navbar);
        viewPager2 = findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(Home_Activity.this);

        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);


        nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id)
                {
                    case R.id.nav_Home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.nav_workout:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.nav_homies:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.nav_tools:
                        viewPager2.setCurrentItem(3);
                        break;
                    case R.id.nav_profile:
                        viewPager2.setCurrentItem(4);
                        break;

                }
                return false;
            }
        });




        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                if (f instanceof profile_fragment) {
                    ((profile_fragment) f).setOnLogoutClickListener(Home_Activity.this);
                }
            }

            @Override
            public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                if (f instanceof profile_fragment) {
                    ((profile_fragment) f).setOnLogoutClickListener(null);
                }
            }
        }, true);


        FirebaseUser firebaseUser = authUser.getCurrentUser();

        String userID = firebaseUser.getUid();
        DatabaseReference referecedProfile = FirebaseDatabase.getInstance().getReference("Registered User Details");
        referecedProfile.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AddFetchUserDetails fetchUserDetails = snapshot.getValue(AddFetchUserDetails.class);
                if(fetchUserDetails!=null)
                {
                    userFirstNameinDB = fetchUserDetails.textFirstName;
                    userLastNameinDB = fetchUserDetails.textLastName;
                    onFirstNameLastNameFetched(userFirstNameinDB, userLastNameinDB);

                    Log.i("Logged User", "Below User is Logged In");
                    Log.i("First Name: ", userFirstNameinDB);
                    Log.i("First Name: ", userLastNameinDB);
                    Log.i("First Name: ", fetchUserDetails.textEmail);




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }






    @Override
    public void onFirstNameLastNameFetched(String firstName, String lastName) {

        nameViewModel.setFullName(firstName, lastName);

//        getSupportFragmentManager().executePendingTransactions();
//        home_fragment homeFragment = (home_fragment) getSupportFragmentManager().findFragmentByTag("f0");
//        if(homeFragment!=null)
//        {
//            homeFragment.updateName(firstName, lastName);
//
//        }


    }

    @Override
    public void onLogout() {
       FirebaseAuth.getInstance().signOut();
        Intent backtoLogin = new Intent(this, Login_activity.class);
        startActivity(backtoLogin);
        finish();

    }
}