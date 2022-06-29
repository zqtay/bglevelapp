package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Tab and fragments
        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new AppTabAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == AppTabAdapter.TAB_FORM_POSITION) {
                    tabAdapter.formFragment.displayFormValues();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

}

