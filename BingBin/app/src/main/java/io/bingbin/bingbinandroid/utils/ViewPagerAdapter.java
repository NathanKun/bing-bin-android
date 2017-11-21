package io.bingbin.bingbinandroid.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.bingbin.bingbinandroid.CameraFragment;
import io.bingbin.bingbinandroid.HomeFragment;
import io.bingbin.bingbinandroid.UserFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CameraFragment.newInstance();
            case 1:
                return HomeFragment.newInstance("1", "Page # 2");
            case 2:
                return UserFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
