package com.example.testwhatatrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    // duration variable static
    private static int SPLASH_SCREEN = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        // variabels for animation
        Animation topAnimation,bottomAnumation;
        ImageView pic,name;


        //linking ids of splash screen
        pic = findViewById(R.id.logopic);
        name = findViewById(R.id.logoname);


        // Animation Effects for splash screen
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnumation = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        // setting animations
        pic.setAnimation(topAnimation);
        name.setAnimation(bottomAnumation);

        // moving to next activity dashboard

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Signin.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}