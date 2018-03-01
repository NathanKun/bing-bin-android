package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.catprogrammer.android.utils.AnimationUtil;

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
import io.bingbin.bingbinandroid.utils.AvatarHelper;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.UserUtil;


/**
 * EcoPointFragment.
 *
 * @author Junyang HE
 */
public class EcoPointFragment extends Fragment {

    final private String[] KEYS = new String[]{"date", "category", "point"};
    final private int[] IDS = new int[]{R.id.historylist_date, R.id.historylist_category, R.id.historylist_point};

    @BindView(R.id.ecopoint_avatar_imageview)
    ImageView ecopointAvatarImageview;
    @BindView(R.id.ecopoint_username_textview)
    TextView ecopointUsernameTextview;
    @BindView(R.id.ecopoint_ecopoint_textview)
    TextView ecopointEcopointTextview;
    @BindView(R.id.ecopoint_sunpoint_textview)
    TextView ecopointSunpointTextview;
    @BindView(R.id.ecopoint_sunimage_imageview)
    ImageView ecopointSunimageImageview;
    @BindView(R.id.ecopoint_masterlayout)
    ConstraintLayout ecopointMasterlayout;

    @BindView(R.id.ecopoint_icon_img_1)
    AppCompatImageView ecopointIconImg1;
    @BindView(R.id.ecopoint_icon_img_2)
    AppCompatImageView ecopointIconImg2;
    @BindView(R.id.ecopoint_icon_img_3)
    AppCompatImageView ecopointIconImg3;
    @BindView(R.id.ecopoint_icon_img_4)
    AppCompatImageView ecopointIconImg4;
    @BindView(R.id.ecopoint_icon_img_5)
    AppCompatImageView ecopointIconImg5;
    @BindView(R.id.ecopoint_icon_img_6)
    AppCompatImageView ecopointIconImg6;
    @BindView(R.id.ecopoint_icon_img_7)
    AppCompatImageView ecopointIconImg7;
    @BindView(R.id.ecopoint_icon_img_8)
    AppCompatImageView ecopointIconImg8;
    @BindView(R.id.ecopoint_icon_img_9)
    AppCompatImageView ecopointIconImg9;
    @BindView(R.id.ecopoint_icon_img_10)
    AppCompatImageView ecopointIconImg10;
    @BindView(R.id.ecopoint_icon_img_11)
    AppCompatImageView ecopointIconImg11;
    @BindView(R.id.ecopoint_icon_img_12)
    AppCompatImageView ecopointIconImg12;

    @BindView(R.id.ecopoint_count_text_1)
    TextView ecopointCountText1;
    @BindView(R.id.ecopoint_count_text_2)
    TextView ecopointCountText2;
    @BindView(R.id.ecopoint_count_text_3)
    TextView ecopointCountText3;
    @BindView(R.id.ecopoint_count_text_4)
    TextView ecopointCountText4;
    @BindView(R.id.ecopoint_count_text_5)
    TextView ecopointCountText5;
    @BindView(R.id.ecopoint_count_text_6)
    TextView ecopointCountText6;
    @BindView(R.id.ecopoint_count_text_7)
    TextView ecopointCountText7;
    @BindView(R.id.ecopoint_count_text_8)
    TextView ecopointCountText8;
    @BindView(R.id.ecopoint_count_text_9)
    TextView ecopointCountText9;
    @BindView(R.id.ecopoint_count_text_10)
    TextView ecopointCountText10;
    @BindView(R.id.ecopoint_count_text_11)
    TextView ecopointCountText11;
    @BindView(R.id.ecopoint_count_text_12)
    TextView ecopointCountText12;

    @BindView(R.id.ecopoint_gridlayout_masterlayout)
    FrameLayout ecopointGridlayoutMasterLayout;
    @BindView(R.id.ecopoint_gridlayout_cover)
    View ecopointGridlayoutCover;
    @BindView(R.id.ecopoint_historylistView)
    ListView ecopointHistorylistView;
    @BindView(R.id.ecopoint_history_swiperefresh)
    SwipeRefreshLayout ecopointHistorySwiperefresh;

    private MainActivity activity;
    private Unbinder unbinder;
    private SimpleAdapter mAdapter;
    private List<Map<String, Object>> recycleHistoryDataToShow = new ArrayList<>();

    public EcoPointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EcoPointFragment.
     */
    public static EcoPointFragment newInstance() {
        return new EcoPointFragment();
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
        View view = inflater.inflate(R.layout.fragment_ecopoint, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        SmartUser user = activity.getCurrentUser();

        // hide gridlayout,  listview
        ecopointGridlayoutMasterLayout.setVisibility(View.INVISIBLE);
        ecopointHistorySwiperefresh.setVisibility(View.GONE);


        // ------ show user info ------

        // generate avatar
        ecopointAvatarImageview.setImageBitmap(AvatarHelper.generateAvarat(
                activity, user.getRabbit(), user.getLeaf()));

        // show username
        ecopointUsernameTextview.setText(user.getFirstName());

        // show eco point and sun point
        ecopointEcopointTextview.setText(String.valueOf(user.getEcoPoint()));
        ecopointSunpointTextview.setText(String.valueOf(user.getSunPoint()));


        // ------ grid view ------

        // adjust grid icon size
        AppCompatImageView[] imgs = {ecopointIconImg1, ecopointIconImg2, ecopointIconImg3,
                ecopointIconImg4, ecopointIconImg5, ecopointIconImg6,
                ecopointIconImg7, ecopointIconImg8, ecopointIconImg9,
                ecopointIconImg10, ecopointIconImg11, ecopointIconImg12
        };

        int maxHeight = ecopointGridlayoutMasterLayout.getHeight();
        int numberHeight = ecopointCountText1.getHeight();
        int targetHeight = (maxHeight - 4 * (numberHeight + 16)) / 4;
        int maxWidth = ecopointGridlayoutMasterLayout.getWidth();
        int targetWidth = (maxWidth - 4 * 16) / 3;
        int target = targetHeight < targetWidth ? targetHeight : targetWidth;
        target = target > 200 ? 200 : target;

        Log.d("Ecopoint grid newHeight", String.valueOf(target));

        for (AppCompatImageView img : imgs) {
            ViewGroup.LayoutParams params = img.getLayoutParams();
            params.height = target;
            params.width = target;
            img.setLayoutParams(params);
        }


        // ------ listview ------

        // add swipe down refresh listener, call getRecycleHistoryData() when swipe down
        ecopointHistorySwiperefresh.setOnRefreshListener(() -> getRecycleHistoryData(user.getToken()));

        // add adapter to listview, link recycleHistoryDataToShow to adapter
        mAdapter = new SimpleAdapter(this.activity, recycleHistoryDataToShow, R.layout.listview_historylist,
                KEYS, IDS);
        ecopointHistorylistView.setAdapter(mAdapter);


        // ------ listener to switch between grid and list ------

        ecopointGridlayoutCover.setOnClickListener((v) -> {
            Log.d("grid", "on click");
            activity.enableInput(false);
            AnimationUtil.revealView(ecopointGridlayoutMasterLayout, false, 500);
            AnimationUtil.revealView(ecopointHistorySwiperefresh, true, 500);
            activity.handler.postDelayed(() -> activity.enableInput(true), 1000);
        });
        ecopointHistorylistView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("listview", "on click");
                activity.enableInput(false);
                AnimationUtil.revealView(ecopointGridlayoutMasterLayout, true, 500);
                AnimationUtil.revealView(ecopointHistorySwiperefresh, false, 500);
                activity.handler.postDelayed(() -> activity.enableInput(true), 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // ------ get data and show ------

        getRecycleHistoryData(user.getToken());
        getRecycleByCategoryData(user.getToken());
        getMyInfoToUpdateUserAndPoints(user.getToken());

        // show gridlayout
        ecopointGridlayoutMasterLayout.setVisibility(View.VISIBLE);

    }

    /**
     * get user info, update user session and info showing
     *
     * @param token BingBinToken of the current user
     */
    private void getMyInfoToUpdateUserAndPoints(String token) {
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity,
                        "Erreur de connexion", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(() -> Toast.makeText(activity,
                            "Request not success", Toast.LENGTH_SHORT).show());
                    return;
                }

                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);

                    if (!json.getBoolean("valid")) {
                        String errorStr = json.getString("error");
                        Log.d("getMyInfo not valid", errorStr);

                        if (errorStr.contains("token")) {
                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity,
                                        "Session expiré", Toast.LENGTH_SHORT).show();
                                activity.handler.postDelayed(() -> {
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    startActivity(intent);
                                    activity.finish();
                                }, 1000);
                            });
                            return;
                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity,
                                    errorStr, Toast.LENGTH_SHORT).show());
                            return;
                        }


                    }

                    // if valid

                    // remove current user
                    SmartUser currentUser = activity.getCurrentUser();
                    SmartLogin smartLogin;
                    if (currentUser != null) {
                        smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                        smartLogin.logout(activity);
                    }

                    // set new user session
                    currentUser = UserUtil.populateBingBinUser(json.getJSONObject("data"), token);
                    UserSessionManager.setUserSession(activity, currentUser);

                    // refresh user info
                    SmartUser finalCurrentUser = currentUser;
                    activity.runOnUiThread(() -> {
                        ecopointEcopointTextview.setText(String.valueOf(finalCurrentUser.getEcoPoint()));
                        ecopointSunpointTextview.setText(String.valueOf(finalCurrentUser.getSunPoint()));

                        ecopointAvatarImageview.setImageBitmap(AvatarHelper.generateAvarat(
                                activity, finalCurrentUser.getRabbit(), finalCurrentUser.getLeaf()));
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(() -> Toast.makeText(activity,
                            "JSON parse error", Toast.LENGTH_SHORT).show());
                }
            }
        };

        activity.bbh.getMyInfo(cb, activity.getCurrentUser().getToken());
    }

    /**
     * asyn get recycle history data, call showRecycleHistoryData() to show in listview if success
     *
     * @param token BingBinToken of the current user
     */
    private void getRecycleHistoryData(String token) {
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        };

        activity.runOnUiThread(() -> ecopointHistorySwiperefresh.setRefreshing(true));
        try {
            showRecycleHistoryData(new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //activity.bbh.getScanHistory(cb, token);
    }

    /**
     * show recycle history in listview
     *
     * @param array json array contains history
     * @throws JSONException json exception
     */
    private void showRecycleHistoryData(JSONArray array) throws JSONException {
        recycleHistoryDataToShow.clear();

        /*for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();

            String date = json.getString("date");
            String category = json.getString("category");
            int pt = json.getInt("point");

            map.put("date", date);
            map.put("category", category);
            map.put("point", pt);
            recycleHistoryDataToShow.add(map);
        }*/

        // TODO delete below when interface is ready
        Map<String, Object> map = new HashMap<>();
        map.put("date", "01.02.18");
        map.put("category", "plastique");
        map.put("point", "35 pts");
        recycleHistoryDataToShow.add(map);
        map = new HashMap<>();
        map.put("date", "02.02.18");
        map.put("category", "metal");
        map.put("point", "5 pts");
        recycleHistoryDataToShow.add(map);
        map = new HashMap<>();
        map.put("date", "03.02.18");
        map.put("category", "hahaha");
        map.put("point", "233 pts");
        recycleHistoryDataToShow.add(map);
        // TODO delete above when interface is ready

        // show data in list
        mAdapter.notifyDataSetChanged();
        ecopointHistorySwiperefresh.setRefreshing(false);
    }

    /**
     * asyn get recycle count by category data and show
     *
     * @param token BingBinToken of the current user
     */
    private void getRecycleByCategoryData(String token) {
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        };
        //activity.bbh.getCountByCategory(cb, token);

        // TODO delete below when interface is ready
        try {
            showRecycleHistoryData(new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
