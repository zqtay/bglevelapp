package com.example.myapp.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AppFragmentPagerAdapter extends FragmentPagerAdapter {

    public AppFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FormFragment();
            case 1:
                return new RecordsFragment();
            default:
                Log.d("debug","Invalid tab index");
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Form";
            case 1:
                return "Records";
            default:
                Log.d("debug","Invalid tab index");
        }
        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }
}
