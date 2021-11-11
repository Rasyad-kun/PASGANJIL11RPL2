package com.example.pasganjil11rpl2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pasganjil11rpl2.Activities.MainActivity;
import com.example.pasganjil11rpl2.Intro.IntroActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        ImageView imageView = findViewById(R.id.ic_logo);
        Glide.with(this)
                .load(R.drawable.ic_logo)
                .fitCenter()
                .centerCrop()
                .into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                finish();
            }
        }, 3000);
    }
}