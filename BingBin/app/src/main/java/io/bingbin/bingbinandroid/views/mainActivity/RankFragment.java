package io.bingbin.bingbinandroid.views.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * User fragment.
 * Contains user's information and logout button.
 *
 * @author Junyang HE
 */
public class RankFragment extends Fragment {

    @BindView(R.id.rank_ranklistView)
    ListView listView;
    @BindView(R.id.rank_swiperefresh)
    SwipeRefreshLayout rankSwiperefresh;

    private MainActivity activity;
    private Unbinder unbinder;

    private SimpleAdapter mAdapter;

    private String[] KEYS = new String[]{"username", "point", "rank"};
    private int[] IDS = new int[]{R.id.listview_username,
            R.id.listview_ecopoint, R.id.listview_rank};

    private List<Map<String, Object>> dataToShow = new ArrayList<>();
    private List<String> avatarsUrl = new ArrayList<>();

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

        // add swipe down refresh listener, call getData() when swipe down
        rankSwiperefresh.setOnRefreshListener(() -> getData());

        // add adapter to listview, link dataToShow to adapter
        mAdapter = new SimpleAdapter(this.activity, dataToShow, R.layout.listview_ranklist,
                KEYS, IDS) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // load avatar
                String url = avatarsUrl.get(position);
                AppCompatImageView iv = view.findViewById(R.id.listview_avatar);
                if (StringUtils.isNotBlank(url) && !StringUtils.equals("null", url)) {
                    Glide.with(RankFragment.this)
                            .load(url)
                            .fitCenter()
                            .into(iv);
                } else {
                    iv.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }

                return view;
            }

        };
        listView.setAdapter(mAdapter);

        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Use BingBinHttp class to get ladder, then call showData()
     */
    private void getData() {
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Get Ladder", "Call Failed");
                rankSwiperefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("Get Ladder", "Request not successful");
                    rankSwiperefresh.setRefreshing(false);
                    return;
                }

                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);

                    if (!json.getBoolean("valid")) {
                        Log.d("Get Ladder", "Not valid: " + json.getString("error"));
                        rankSwiperefresh.setRefreshing(false);
                        return;
                    }

                    JSONObject ladder = json.getJSONObject("ladder");
                    JSONArray data = new JSONArray();
                    Iterator it = ladder.keys();
                    while (it.hasNext()) {
                        data.put(ladder.getJSONObject((String) it.next()));
                    }

                    Log.d("Get Ladder", "data size: " + data.length());
                    Log.d("Get Ladder", "data: " + data.toString());

                    activity.runOnUiThread(() -> {
                        try {
                            showData(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Get Ladder", "Showdata JSON parse error");
                            rankSwiperefresh.setRefreshing(false);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Get Ladder", "JSON parse error");
                    rankSwiperefresh.setRefreshing(false);
                }
            }
        };
        activity.bbh.getLadder(cb, activity.getCurrentUser().getToken(), "all");
    }

    /**
     * show ladder in listview
     * @param array json array contains ladder
     * @throws JSONException    json exception
     */
    private void showData(JSONArray array) throws JSONException {
        dataToShow.clear();
        avatarsUrl.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();

            String name = json.getString("name");
            int rank = json.getInt("rank");
            int pt = json.getInt("eco_point");

            map.put("username", name);
            map.put("point", pt);
            map.put("rank", rank);
            dataToShow.add(map);

            String avatarUrl = json.getString("img_url");
            avatarsUrl.add(avatarUrl);
        }

        // show data in list
        mAdapter.notifyDataSetChanged();
        rankSwiperefresh.setRefreshing(false);


        if (BuildConfig.DEBUG && listView.getCount() != avatarsUrl.size()) {
            Log.d("size error", "listView.getCount() != urls.size()");
            Log.d("size error", "listView.getCount() = " + listView.getCount());
            Log.d("size error", "urls.size() = " + avatarsUrl.size());
        }
    }
}
