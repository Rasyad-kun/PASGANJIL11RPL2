package com.example.pasganjil11rpl2.Intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pasganjil11rpl2.Activities.MainActivity;
import com.example.pasganjil11rpl2.Activities.NavigationActivity;
import com.example.pasganjil11rpl2.R;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    private ViewPager slider;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button nextBtn;
    private Button prevBtn;
    private int currentPage;
    private SlideAdapter slideAdapter;
    private String btnClick = "btnClicked";

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(btnClick, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(btnClick, Boolean.TRUE);
            editor.apply();
        } else {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        slider = findViewById(R.id.viewpager);
        dotsLayout = findViewById(R.id.dotsLayout);

        nextBtn = findViewById(R.id.next_button);
        prevBtn = findViewById(R.id.prev_button);

        slideAdapter = new SlideAdapter(this);

        slider.setAdapter(slideAdapter);

        addDotIndicator(0);

        slider.addOnPageChangeListener(viewListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < dots.length - 1) {
                    slider.setCurrentItem(currentPage + 1);
                } else if (currentPage == dots.length - 1) {
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    slider.setCurrentItem(currentPage - 1);
                }
            }
        });
    }

    public void addDotIndicator(int position) {

        dots = new TextView[slideAdapter.slide_headings.length];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent_white));

            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            currentPage = position;
            if (position == 0) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                nextBtn.setText("Next");
                prevBtn.setVisibility(View.GONE);
            } else if (position == dots.length - 1) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                nextBtn.setText("Finish");
                prevBtn.setVisibility(View.VISIBLE);
            } else {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                nextBtn.setText("Next");
                prevBtn.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}