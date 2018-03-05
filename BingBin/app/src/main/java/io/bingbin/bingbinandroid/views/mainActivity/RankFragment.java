package io.bingbin.bingbinandroid.views.mainActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.catprogrammer.android.utils.AnimationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.AvatarHelper;
import io.bingbin.bingbinandroid.utils.BingBinCallback;
import io.bingbin.bingbinandroid.utils.BingBinCallbackAction;


/**
 * User fragment.
 * Contains user's information and logout button.
 *
 * @author Junyang HE
 */
@SuppressWarnings("FieldCanBeLocal")
public class RankFragment extends Fragment {

    final private String BBH_DURATION_ALL = "all";
    final private String BBH_DURATION_DAY = "day";
    final private String BBH_DURATION_WEEK = "week";
    final private String BBH_DURATION_MONTH = "month";

    final private String[] KEYS = new String[]{"username", "point", "rank", "avatar"};
    final private int[] IDS = new int[]{R.id.listview_username,
            R.id.listview_ecopoint, R.id.listview_rank, R.id.listview_avatar};

    @BindView(R.id.rank_ranklistView)
    ListView listView;
    @BindView(R.id.rank_swiperefresh)
    SwipeRefreshLayout rankSwiperefresh;
    @BindView(R.id.rank_buttonbar_all_btn)
    Button rankButbtonbarAllBtn;
    @BindView(R.id.rank_butbtonbar_day_btn)
    Button rankButbtonbarDayBtn;
    @BindView(R.id.rank_butbtonbar_week_btn)
    Button rankButbtonbarWeekBtn;
    @BindView(R.id.rank_butbtonbar_month_btn)
    Button rankButbtonbarMonthBtn;
    @BindView(R.id.ranking_sendsun_layout)
    ConstraintLayout rankingSendsunLayout;
    @BindView(R.id.rank_title_suncount)
    TextView rankTitleSuncount;

    private MainActivity activity;
    private Unbinder unbinder;

    private SimpleAdapter mAdapter;

    private List<Map<String, Object>> dataToShow = new ArrayList<>();

    private String currentDuration = "all";


    public RankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RankFragment.
     */
    public static RankFragment newInstance() {
        return new RankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        activity = (MainActivity) getActivity();
        assert activity != null;

        // show user sunPt count
        String sunPoint = activity.getCurrentUser().getSunPoint() + "x";
        rankTitleSuncount.setText(sunPoint);

        // Make AllBtn green, look likes clicked
        rankButbtonbarAllBtn.setTextColor(getResources().getColor(R.color.primary_color));

        // add swipe down refresh listener, call getData() when swipe down
        rankSwiperefresh.setOnRefreshListener(() -> getData(currentDuration));

        // add adapter to listview, link dataToShow to adapter
        mAdapter = new SimpleAdapter(this.activity, dataToShow, R.layout.listview_ranklist,
                KEYS, IDS) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView sunButton = view.findViewById(R.id.listview_sun);
                sunButton.setOnClickListener((v) -> {
                    Log.d("selected user id", (String) dataToShow.get(position).get("id"));
                    activity.showLoader(true);

                    BingBinCallbackAction action = new BingBinCallbackAction() {
                        @Override
                        public void onFailure() {
                            activity.runOnUiThread(() -> Toast.makeText(
                                    activity.getApplicationContext(),
                                    R.string.bingbinhttp_onfailure,
                                    Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponseNotSuccess() {
                            activity.runOnUiThread(() -> Toast.makeText(
                                    activity.getApplicationContext(),
                                    R.string.bingbinhttp_onresponsenotsuccess,
                                    Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onJsonParseError() {
                            activity.runOnUiThread(() -> Toast.makeText(
                                    activity.getApplicationContext(),
                                    R.string.bingbinhttp_onjsonparseerror,
                                    Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onNotValid(String errorStr) {
                            activity.runOnUiThread(() -> Toast.makeText(
                                    activity.getApplicationContext(),
                                    errorStr,
                                    Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onTokenNotValid(String errorStr) {
                            activity.runOnUiThread(() -> Toast.makeText(
                                    activity.getApplicationContext(),
                                    errorStr,
                                    Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onValid(JSONObject json) throws JSONException {
                            if (json.getBoolean("limit_reach")) {
                                activity.runOnUiThread(() -> {
                                    Toast.makeText(activity.getApplicationContext(),
                                            "Vous ne pouvez qu'envoyer 5 soleil par jour",
                                            Toast.LENGTH_SHORT).show();
                                    activity.showLoader(false);
                                });
                                return;
                            }

                            activity.runOnUiThread(() -> {
                                activity.showLoader(false);
                                AnimationUtil.revealView(sunButton, false, 500); // hide sun button

                                // disable input + fade in => 0.5s => delay 2s => fade out => 0.5s => enable input
                                activity.enableInput(false);
                                AnimationUtil.revealView(rankingSendsunLayout, true, 500);
                                Handler handler = new Handler();
                                handler.postDelayed(
                                        () -> AnimationUtil.revealView(rankingSendsunLayout, false, 500), 1500);
                                handler.postDelayed(
                                        () -> activity.enableInput(true), 2500);
                            });
                        }

                        @Override
                        public void onAnyError() {
                            activity.runOnUiThread(() -> activity.showLoader(false));
                        }
                    };

                    activity.bbh.sendSunPoint(new BingBinCallback(action),
                            activity.getCurrentUser().getToken(),
                            (String) dataToShow.get(position).get("id"));

                }); // onClickListener end

                if((boolean)dataToShow.get(position).get("isSent")) {
                    sunButton.setVisibility(View.GONE);
                }

                return view;
            }

        }; // new SimpleAdapter end

        mAdapter.setViewBinder((view, data, textRepresentation) -> {
            if (view instanceof ImageView && data instanceof Bitmap) {
                ImageView i = (ImageView) view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        });
        listView.setAdapter(mAdapter);

        getData(BBH_DURATION_ALL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Use BingBinHttp class to get ladder, then call showData()
     */
    private void getData(String duration) {
        BingBinCallbackAction action = new BingBinCallbackAction() {
            @Override
            public void onFailure() {
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                        R.string.bingbinhttp_onfailure, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponseNotSuccess() {
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                        R.string.bingbinhttp_onresponsenotsuccess, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onJsonParseError() {
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                        R.string.bingbinhttp_onjsonparseerror, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onNotValid(String errorStr) {
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                        errorStr, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onTokenNotValid(String errorStr) {
                activity.backToLoginActivity();
            }

            @Override
            public void onValid(JSONObject json) throws JSONException {
                JSONArray data = new JSONArray();
                Object ladder = json.get("ladder");
                if (ladder instanceof JSONObject) { // if no data, will be [] JSONArray
                    Iterator it = ((JSONObject) ladder).keys();
                    while (it.hasNext()) {
                        data.put(((JSONObject) ladder).getJSONObject((String) it.next()));
                    }
                }

                Log.d("Get Ladder", "data size: " + data.length());
                Log.d("Get Ladder", "data: " + data.toString());

                showData(data);

                activity.runOnUiThread(() -> {
                    if (rankSwiperefresh != null) {
                        rankSwiperefresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onAnyError() {
                activity.runOnUiThread(() -> {
                    if (rankSwiperefresh != null) {
                        rankSwiperefresh.setRefreshing(false);
                    }
                });
            }
        };

        activity.runOnUiThread(() -> rankSwiperefresh.setRefreshing(true));
        activity.bbh.getLadder(new BingBinCallback(action),
                activity.getCurrentUser().getToken(),
                duration);
    }

    /**
     * show ladder in listview
     *
     * @param array json array contains ladder
     * @throws JSONException json exception
     */
    private void showData(JSONArray array) throws JSONException {
        dataToShow.clear();
        //avatarsUrl.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();

            String name = json.getString("firstname");
            String id = json.getString("id");
            int rank = json.getInt("rank");
            int pt = json.getInt("eco_point");
            int rabbitId = json.getInt("id_rabbit");
            int leafId = json.getInt("id_leaf");
            boolean isSent = json.getBoolean("has_receive_sun_point");

            Bitmap avatar = AvatarHelper.generateAvatar(activity, rabbitId, leafId);

            map.put("username", name);
            map.put("point", pt);
            map.put("rank", rank);
            map.put("avatar", avatar);
            map.put("id", id);
            map.put("isSent", isSent);
            dataToShow.add(map);
        }

        // show data in list
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rank_buttonbar_all_btn, R.id.rank_butbtonbar_day_btn,
            R.id.rank_butbtonbar_week_btn, R.id.rank_butbtonbar_month_btn})
    void buttonBarBtnOnClick(View view) {
        rankButbtonbarAllBtn.setTextColor(getResources().getColor(R.color.black));
        rankButbtonbarDayBtn.setTextColor(getResources().getColor(R.color.black));
        rankButbtonbarWeekBtn.setTextColor(getResources().getColor(R.color.black));
        rankButbtonbarMonthBtn.setTextColor(getResources().getColor(R.color.black));

        switch (view.getId()) {
            case R.id.rank_buttonbar_all_btn:
                currentDuration = BBH_DURATION_ALL;
                rankButbtonbarAllBtn.setTextColor(getResources().getColor(R.color.primary_color));
                getData(currentDuration);
                break;
            case R.id.rank_butbtonbar_day_btn:
                currentDuration = BBH_DURATION_DAY;
                rankButbtonbarDayBtn.setTextColor(getResources().getColor(R.color.primary_color));
                getData(currentDuration);
                break;
            case R.id.rank_butbtonbar_week_btn:
                currentDuration = BBH_DURATION_WEEK;
                rankButbtonbarWeekBtn.setTextColor(getResources().getColor(R.color.primary_color));
                getData(currentDuration);
                break;
            case R.id.rank_butbtonbar_month_btn:
                currentDuration = BBH_DURATION_MONTH;
                rankButbtonbarMonthBtn.setTextColor(getResources().getColor(R.color.primary_color));
                getData(currentDuration);
                break;
        }
    }
}
