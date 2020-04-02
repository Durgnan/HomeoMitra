package com.homeomitra.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView img = findViewById(R.id.logo);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_screen_fade);

        img.startAnimation(aniFade);
        pref = getSharedPreferences("HomeoMitra",MODE_PRIVATE);
        handler = new Handler();
       final String isUserLoggedIn = pref.getString("username","");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isUserLoggedIn.equals(""))
                    startActivity(new Intent(SplashActivity.this, ValidationActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));



                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                finish();

            }
        },1500);

    }
}
