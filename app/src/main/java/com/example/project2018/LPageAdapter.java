package com.example.project2018;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.List;

class LPageAdapter extends FragmentStatePagerAdapter {
    private String[] titles;

    public LPageAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
        Log.i("adapter","new ");
    }

    private List<Fragment> fragments;
    @Override
    public Fragment getItem(int i) {
        Log.i("adapter","fr ");
        return fragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Log.i("adapter","ti");
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
