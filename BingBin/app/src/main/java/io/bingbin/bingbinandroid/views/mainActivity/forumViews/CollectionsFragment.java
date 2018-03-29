package io.bingbin.bingbinandroid.views.mainActivity.forumViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.bingbin.bingbinandroid.R;

/**
 * Fragment for my collection posts for ForumFragment in MainActivity
 *
 * @author Junyang HE
 */
public class CollectionsFragment extends Fragment {


    public CollectionsFragment() {
        // Required empty public constructor
    }

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collections, container, false);
    }

}
