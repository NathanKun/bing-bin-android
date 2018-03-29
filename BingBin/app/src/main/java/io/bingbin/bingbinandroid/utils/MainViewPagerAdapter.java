package io.bingbin.bingbinandroid.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import io.bingbin.bingbinandroid.views.mainActivity.eventViews.EventFragment;
import io.bingbin.bingbinandroid.views.mainActivity.forumViews.ForumFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.EcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.GetEcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.RankFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.RecognitionFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.WelcomeFragment;

/**
 * Adapter for ViewPager
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MainViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 3;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventFragment.newInstance();
            case 1:
                return RecognitionFragment.newInstance();
            case 2:
                return ForumFragment.newInstance();
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
