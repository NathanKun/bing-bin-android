package io.bingbin.bingbinandroid.views.mainActivity.forumViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.ForumViewPagerAdapter;
import io.bingbin.bingbinandroid.utils.RecognitionViewPagerAdapter;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    MainActivity activity;

    @BindView(R.id.forum_viewpager)
    public ViewPager viewPager;
    private ForumViewPagerAdapter adapter;

    private Unbinder unbinder;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance() {
        return new ForumFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        unbinder = ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();

        // init viewpager
        adapter = new ForumViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        return view;
    }

}
