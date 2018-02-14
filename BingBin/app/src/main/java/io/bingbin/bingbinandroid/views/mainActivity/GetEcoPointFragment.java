package io.bingbin.bingbinandroid.views.mainActivity;

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
 * EcoPointFragment.
 *
 * @author Junyang HE
 */
public class GetEcoPointFragment extends Fragment {

    private MainActivity activity;

    private Unbinder unbinder;

    public GetEcoPointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EcoPointFragment.
     */
    public static GetEcoPointFragment newInstance() {
        return new GetEcoPointFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_getecopoint, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
