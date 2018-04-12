package io.bingbin.bingbinandroid.views.mainActivity.forumViews;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    private Unbinder unbinder;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance() {
        return new ForumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
    }

}
