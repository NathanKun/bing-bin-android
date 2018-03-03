package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;


/**
 * EcoPointFragment.
 *
 * @author Junyang HE
 */
public class GetEcoPointFragment extends Fragment {

    @BindView(R.id.getecopoint_ecopoint_got_textview)
    TextView getecopointEcopointGotTextview;
    @BindView(R.id.getecopoint_share_btn)
    Button getecopointShareBtn;
    @BindView(R.id.getecopoint_myecopoint_btn)
    Button getecopointMyecopointBtn;
    @BindView(R.id.getecopoint_master_layout)
    ConstraintLayout getecopointMasterLayout;

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

    @OnClick(R.id.getecopoint_share_btn)
    void shareOnClick(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Bing Bin app reconnaît les déchets et propose la poubelle appropriée. Le tri n'a jamais été si simple et amusant avec Bing Bin:)";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Trion mieux, vivons mieux - Bing Bin");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @OnClick(R.id.getecopoint_myecopoint_btn)
    void myEcoPointOnClick(View view) {
        activity.viewPager.setCurrentItem(0);
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

    public void setEcoPoint(int ep) {
        // show eco point got
        getecopointMasterLayout.post(() -> getecopointEcopointGotTextview.setText(String.valueOf(ep)));
    }
}
