package io.bingbin.bingbinandroid.views.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;


/**
 * HomeFragment.
 *
 * @author Junyang HE
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.tv_home_welcome)
    TextView tvHomeWelcome;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView10)
    TextView textView10;
    private Unbinder unbinder;

    private MainActivity activity;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        tvHomeWelcome.setText(String.format(activity.getResources().getString(R.string.home_homefragment), activity.getCurrentUser().getUsername()));
        textView.setText("getToken ; " + activity.getCurrentUser().getToken());
        textView2.setText("getAvatarUrl ; " + activity.getCurrentUser().getAvatarUrl());
        textView3.setText("getBirthday ; " + activity.getCurrentUser().getBirthday());
        textView4.setText("getEco_point ; " + activity.getCurrentUser().getEco_point());
        textView5.setText("getEmail ; " + activity.getCurrentUser().getEmail());
        textView6.setText("getFirstName ; " + activity.getCurrentUser().getFirstName());
        textView7.setText("getLastName ; " + activity.getCurrentUser().getLastName());
        textView8.setText("getPseudo ; " + activity.getCurrentUser().getPseudo());
        textView9.setText("getUsername ; " + activity.getCurrentUser().getUsername());
        textView10.setText("getUserId ; " + activity.getCurrentUser().getUserId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
