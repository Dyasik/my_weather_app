package com.example.user.myapplication2;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final int PAGES_COUNT = 2;

    private Resources res;

    public SectionsPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        this.res = res;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0: return new NowFragment();
            case 1: return new ForecastFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return PAGES_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return res.getString(R.string.tab1);
            case 1:
                return res.getString(R.string.tab2);
        }
        return null;
    }
}
