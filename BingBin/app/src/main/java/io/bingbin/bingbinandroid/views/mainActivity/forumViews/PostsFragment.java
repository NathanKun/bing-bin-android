package io.bingbin.bingbinandroid.views.mainActivity.forumViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.bingbin.bingbinandroid.R;

/**
 * Fragment for posts for ForumFragment in MainActivity
 *
 * @author Junyang HE
 */
public class PostsFragment extends Fragment {


    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

}
