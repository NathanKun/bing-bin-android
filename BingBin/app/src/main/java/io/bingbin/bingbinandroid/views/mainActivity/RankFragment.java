package io.bingbin.bingbinandroid.views.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinCallBack;
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

    @BindView(R.id.listView)
    ListView listView;

    private MainActivity activity;
    private Unbinder unbinder;

    private SimpleAdapter mAdapter;
    private String[] KEYS = new String[]{"avatar", "username", "point"};
    private int[] IDS = new int[]{R.id.listview_avatar, R.id.listview_username, R.id.listview_ecopoint};

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

        Runnable onFailure = () -> {
            System.out.println("onFailure");
        };

        Runnable onResponseNotSuccess = () -> {
            System.out.println("onResponseNotSuccess");
        };

        Runnable onJsonParseError = () -> {
            System.out.println("onJsonParseError");
        };

        Runnable onNotValid = () -> {
            System.out.println("onNotValid");
        };

        Runnable onValid = () -> {
            System.out.println("onValid");
        };

        BingBinCallBack cb = new BingBinCallBack(
                onFailure, onResponseNotSuccess, onJsonParseError, onNotValid, onValid
        );
        activity.bbh.getLadder(cb, activity.getCurrentUser().getToken(), "all");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showData(JSONArray array) throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> urls = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();

            String name = json.getString("name");
            int pt = json.getInt("eco_point");

            map.put("username", name);
            map.put("point", pt);
            data.add(map);

            String avatarUrl = json.getString("image_url");
            urls.add(avatarUrl);
        }

        // show data in list
        mAdapter = new SimpleAdapter(this.activity, data, R.layout.listview_ranklist,
                KEYS, IDS);
        listView.setAdapter(mAdapter);

        if(BuildConfig.DEBUG && listView.getCount() != urls.size()) {
            Log.d("size error", "listView.getCount() != urls.size()");
            Log.d("size error", "listView.getCount() = " + listView.getCount());
            Log.d("size error", "urls.size() = " + urls.size());
        }

        // load avatars
        for(int i = 0; i < listView.getCount(); i++) {
            String url = urls.get(i);
            if(StringUtils.isNotBlank(url) && !StringUtils.equals("null", url)) {
                ImageView iv = listView.getChildAt(i).findViewById(R.id.listview_avatar);
                Glide.with(this)
                        .load(url)
                        .fitCenter()
                        .into(iv);
            }
        }
    }
}
