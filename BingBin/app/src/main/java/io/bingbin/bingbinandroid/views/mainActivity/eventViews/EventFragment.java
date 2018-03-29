package io.bingbin.bingbinandroid.views.mainActivity.eventViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.RecognitionViewPagerAdapter;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {



    MainActivity activity;

    private RecognitionViewPagerAdapter adapter;

    private Unbinder unbinder;
    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        unbinder = ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
