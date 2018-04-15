package com.example.habibaabbasii.testcase.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.habibaabbasii.testcase.ui.fragments.AllAdsFragment;
import com.example.habibaabbasii.testcase.ui.fragments.EditProfileFragment;
import com.example.habibaabbasii.testcase.ui.fragments.HomeFragment;
import com.example.habibaabbasii.testcase.R;
import com.example.habibaabbasii.testcase.databinding.ActivityHomeBinding;
import com.example.habibaabbasii.testcase.ui.activities.AuthenticationActivity;
import com.example.habibaabbasii.testcase.ui.fragments.MyAdsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, DrawerLayout.DrawerListener
, HomeFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener ,
        MyAdsFragment.OnFragmentInteractionListener,
        AllAdsFragment.OnFragmentInteractionListener{

    FirebaseAuth mAuth;
    ActivityHomeBinding binding;

    FragmentManager mng;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mng = getSupportFragmentManager();

        mng.beginTransaction()
                .replace(R.id.content, new HomeFragment())
                .commit();


        binding.drawer.addDrawerListener(this);
        setupListeners();
    }

    private void setupListeners() {
        binding.home.setOnClickListener(this);
        binding.editProfile.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        binding.drawer.closeDrawer(GravityCompat.START);
        this.view = view;

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {


    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {


        if (view == binding.home) {

            mng.beginTransaction()
                    .replace(R.id.content, new HomeFragment())
                    .commit();
        } else if (view == binding.editProfile) {

            mng.beginTransaction()
                    .replace(R.id.content, new EditProfileFragment())
                    .commit();
        } else if (view == binding.logout) {

            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        }

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onHomeFragmentInteraction(int i) {

        switch (i){
            case HomeFragment.OnFragmentInteractionListener.MENU:
                binding.drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onEditProfileFragmentInteraction(int i) {

        switch (i){
            case EditProfileFragment.OnFragmentInteractionListener.MENU:
                binding.drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
