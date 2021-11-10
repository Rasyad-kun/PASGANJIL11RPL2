package com.example.pasganjil11rpl2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

//        ImageView splash_background = findViewById(R.id.splash_background);
//        Glide.with(this).load(R.drawable.background_splash).fitCenter().centerCrop().into(splash_background);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                finish();
//            }
//        }, 3000);
    }
}