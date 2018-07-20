package com.example.habibaabbasii.testcase.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.habibaabbasii.testcase.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends Activity {

    FirebaseAuth mAuth;
Animation animation;
ImageView iV;
//ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding=
                DataBindingUtil.setContentView(this, R.layout.activity_test_main);
        mAuth = FirebaseAuth.getInstance();

        iV= (ImageView) findViewById(R.id.imageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                    animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fade_in);
                    iV.startAnimation(animation);
                 //   overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                    MaterialRippleLayout.on(R.anim.fade_in)
//                            .rippleColor(Color.BLACK)
//                            .create();
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fade_in);
                    iV.startAnimation(animation);
                    //   overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                    finish();
                }
            }
        },5000);

    }
}
