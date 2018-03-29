package io.bingbin.bingbinandroid.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import io.bingbin.bingbinandroid.views.mainActivity.forumViews.CollectionsFragment;
import io.bingbin.bingbinandroid.views.mainActivity.forumViews.MyPostsFragment;
import io.bingbin.bingbinandroid.views.mainActivity.forumViews.PostsFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.EcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.GetEcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.RankFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.WelcomeFragment;

/**
 * Adapter for ViewPager
 */
public class ForumViewPagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ForumViewPagerAdapter(FragmentManager fragmentManager) {
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
                return MyPostsFragment.newInstance();
            case 1:
                return PostsFragment.newInstance();
            case 2:
                return CollectionsFragment.newInstance();
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
