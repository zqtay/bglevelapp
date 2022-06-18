package com.example.myapp.fragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
                return new ViewFragment();
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
                return "View";
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
