package com.example.habibaabbasii.testcase.ui.activities;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.habibaabbasii.testcase.ui.fragments.LoginFragment;
import com.example.habibaabbasii.testcase.R;
import com.example.habibaabbasii.testcase.ui.fragments.SignupFragment;

public class AuthenticationActivity extends AppCompatActivity  implements LoginFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener {

    FragmentManager mng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mng = getSupportFragmentManager();
        mng.beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    @Override
    public void onLoginFragmentInteraction(int uri) {
        switch (uri) {

            case LoginFragment.OnFragmentInteractionListener.HOME:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;

            case LoginFragment.OnFragmentInteractionListener.SIGNUP:
                mng.beginTransaction()
                        .replace(R.id.container, new SignupFragment())
                        .addToBackStack(null)
                        .commit();

        }
    }

    @Override
    public void onSignupFragmentInteraction(int i) {

        switch (i) {

            case SignupFragment.OnFragmentInteractionListener.HOME:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;

        }
    }
}
