package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.fragment.AppTabAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    public static AppTabAdapter tabAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build database
        AppDatabaseService.buildDatabase(getApplicationContext());

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Tab and fragments
        tabAdapter = new AppTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.main_viewPager);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == AppTabAdapter.TAB_INDEX_FORM) {
                    tabAdapter.formFragment.displayFormValues();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = findViewById(R.id.main_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

}

