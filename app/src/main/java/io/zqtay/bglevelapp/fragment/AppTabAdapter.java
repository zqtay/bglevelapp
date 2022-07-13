package io.zqtay.bglevelapp.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AppTabAdapter extends FragmentPagerAdapter {

    public static final int TAB_INDEX_FORM = (int)0;
    public static final int TAB_INDEX_RECORDS = (int)1;
    public static final String TAB_TITLE_FORM = "Form";
    public static final String TAB_TITLE_RECORDS = "Records";

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
            case TAB_INDEX_FORM:
                return formFragment;
            case TAB_INDEX_RECORDS:
                return recordsFragment;
            default:
                Log.d("debug","Invalid tab index");
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_INDEX_FORM:
                return TAB_TITLE_FORM;
            case TAB_INDEX_RECORDS:
                return TAB_TITLE_RECORDS;
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
