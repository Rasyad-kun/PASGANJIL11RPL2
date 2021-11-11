package com.example.pasganjil11rpl2.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pasganjil11rpl2.Fragments.HistoryFragment;
import com.example.pasganjil11rpl2.Fragments.MemoFragment;
import com.example.pasganjil11rpl2.Fragments.NewsFragment;
import com.example.pasganjil11rpl2.Fragments.ProfileFragment;
import com.example.pasganjil11rpl2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setTitle("News");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new NewsFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_news:
                            setTitle("News");
                            selectedFragment = new NewsFragment();
                            break;
                        case R.id.nav_history:
                            setTitle("History");
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_memo:
                            setTitle("Memo");
                            selectedFragment = new MemoFragment();
                            break;
                        case R.id.nav_profile:
                            setTitle("Profile");
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}