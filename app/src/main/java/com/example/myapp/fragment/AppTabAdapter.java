package com.example.myapp.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.text.Normalizer;

public class AppTabAdapter extends FragmentPagerAdapter {

    public static final int TAB_FORM_POSITION = (int)0;
    public static final int TAB_RECORDS_POSITION = (int)1;
    public static final String TAB_FORM_TITLE = "Form";
    public static final String TAB_RECORDS_TITLE = "Records";

    public FormFragment formFragment;
    public RecordsFragment recordsFragment;

    public AppTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
        formFragment = new FormFragment();
        recordsFragment = new RecordsFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_FORM_POSITION:
                return formFragment;
            case TAB_RECORDS_POSITION:
                return recordsFragment;
            default:
                Log.d("debug","Invalid tab index");
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_FORM_POSITION:
                return TAB_FORM_TITLE;
            case TAB_RECORDS_POSITION:
                return TAB_RECORDS_TITLE;
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
