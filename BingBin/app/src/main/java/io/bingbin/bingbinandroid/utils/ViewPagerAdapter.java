package io.bingbin.bingbinandroid.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import io.bingbin.bingbinandroid.views.mainActivity.GetEcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.WelcomeFragment;
import io.bingbin.bingbinandroid.views.mainActivity.EcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.RankFragment;

/**
 * Adapter for ViewPager
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 4;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EcoPointFragment.newInstance();
            case 1:
                return WelcomeFragment.newInstance();
            case 2:
                return RankFragment.newInstance();
            case 3:
                return GetEcoPointFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }



    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
