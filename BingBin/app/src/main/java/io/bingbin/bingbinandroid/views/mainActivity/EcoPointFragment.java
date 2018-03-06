package io.bingbin.bingbinandroid.views.mainActivity;

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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.catprogrammer.android.utils.AnimationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.Category;
import io.bingbin.bingbinandroid.utils.AvatarHelper;
import io.bingbin.bingbinandroid.utils.BingBinCallback;
import io.bingbin.bingbinandroid.utils.BingBinCallbackAction;
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

    @BindView(R.id.ecopoint_historylistView)
    ListView ecopointHistorylistView;
    @BindView(R.id.ecopoint_history_swiperefresh)
    SwipeRefreshLayout ecopointHistorySwiperefresh;
    @BindView(R.id.ecopoint_gridlayout)
    GridLayout ecopointGridlayout;

    private MainActivity activity;
    private Unbinder unbinder;
    private SimpleAdapter mAdapter;
    private final List<Map<String, Object>> recycleHistoryDataToShow = new ArrayList<>();
    private boolean isShowingGrid;

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

        // hide gridlayout, listview
        ecopointGridlayout.setVisibility(View.INVISIBLE);
        ecopointHistorySwiperefresh.setVisibility(View.GONE);


        // ------ show user info ------

        // generate avatar
        ecopointAvatarImageview.setImageBitmap(AvatarHelper.generateAvatar(
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

        int maxHeight = ecopointGridlayout.getHeight();
        int numberHeight = ecopointCountText1.getHeight();
        int targetHeight = (maxHeight - 4 * (numberHeight + 16)) / 4;
        int maxWidth = ecopointGridlayout.getWidth();
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

        ecopointEcopointTextview.setOnClickListener((v) -> {
            isShowingGrid = !isShowingGrid;
            switchBetweenGridAndList(isShowingGrid);
        });


        // ------ listener to change avatar ------
        ecopointAvatarImageview.setOnClickListener((view -> activity.startAvatarActivity()));


        // ------ get data and show ------

        getRecycleHistoryData(user.getToken());
        getRecycleByCategoryData(user.getToken());
        getMyInfoToUpdateUserAndPoints(user.getToken());


        // show gridlayout
        ecopointGridlayout.setVisibility(View.VISIBLE);
        isShowingGrid = true;

    }

    /**
     * Convenient method to switch between grid and list
     *
     * @param showGrid is going to show Grid and hide List
     */
    private void switchBetweenGridAndList(boolean showGrid) {
        activity.enableInput(false);
        AnimationUtil.revealView(ecopointGridlayout, showGrid, 500);
        AnimationUtil.revealView(ecopointHistorySwiperefresh, !showGrid, 500);
        activity.handler.postDelayed(() -> activity.enableInput(true), 750);
    }

    /**
     * get user info, update user session and info showing
     *
     * @param token BingBinToken of the current user
     */
    void getMyInfoToUpdateUserAndPoints(String token) {
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
                Log.d("getMyInfo not valid", errorStr);
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(),
                        errorStr, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onTokenNotValid(String errorStr) {
                activity.backToLoginActivity();
            }

            @Override
            public void onValid(JSONObject json) throws JSONException {
                // remove current user
                activity.removeCurrentUserFromSession();

                // set new user session
                SmartUser currentUser = UserUtil.populateBingBinUser(json, token);
                UserSessionManager.setUserSession(activity, currentUser);

                // refresh user info
                activity.runOnUiThread(() -> {
                    // when swiping real fast, possible that calling these code when view is destroyed
                    if (ecopointEcopointTextview != null && ecopointSunpointTextview != null && ecopointAvatarImageview != null) {
                        ecopointEcopointTextview.setText(String.valueOf(currentUser.getEcoPoint()));
                        ecopointSunpointTextview.setText(String.valueOf(currentUser.getSunPoint()));

                        ecopointAvatarImageview.setImageBitmap(AvatarHelper.generateAvatar(
                                activity, currentUser.getRabbit(), currentUser.getLeaf()));
                    }

                });
            }

            @Override
            public void onAnyError() { }
        };

        activity.bbh.getMyInfo(new BingBinCallback(action), activity.getCurrentUser().getToken());
    }

    /**
     * asyn get recycle history data, call showRecycleHistoryData() to show in listview if success
     *
     * @param token BingBinToken of the current user
     */
    private void getRecycleHistoryData(String token) {
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
                JSONArray historyArray = json.getJSONArray("history");
                activity.runOnUiThread(() -> {
                    try {
                        showRecycleHistoryData(historyArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onJsonParseError();
                    }
                });
            }

            @Override
            public void onAnyError() {
                if (ecopointHistorySwiperefresh != null) {
                    ecopointHistorySwiperefresh.setRefreshing(false);
                }
            }
        };

        activity.runOnUiThread(() -> ecopointHistorySwiperefresh.setRefreshing(true));
        activity.bbh.getMyRecycleHistory(new BingBinCallback(action), token);
    }

    /**
     * show recycle history in listview
     *
     * @param array json array contains history
     * @throws JSONException json exception
     */
    private void showRecycleHistoryData(JSONArray array) throws JSONException {
        recycleHistoryDataToShow.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();

            long timestamp = json.getLong("date_of_scan");
            int id_type = json.getInt("id_type");
            int eco_point = json.getInt("eco_point");

            String date = (new SimpleDateFormat("dd-MM-yy", Locale.FRANCE)).format(new Date(timestamp * 1000));

            map.put("date", date);
            map.put("category", Category.getFrenchNameById(id_type));
            map.put("point", String.valueOf(eco_point) + " pts");
            recycleHistoryDataToShow.add(map);
        }
        mAdapter.notifyDataSetChanged();

        // show data in list
        if (ecopointHistorySwiperefresh != null) {
            ecopointHistorySwiperefresh.setRefreshing(false);
        }

    }

    /**
     * asyn get recycle count by category data and show
     *
     * @param token BingBinToken of the current user
     */
    private void getRecycleByCategoryData(String token) {
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
               JSONArray historyArray = json.getJSONArray("summary");
               showRecycleCountData(historyArray);
           }

           @Override
           public void onAnyError() { }
       };

        activity.bbh.getMyRecycleCounts(new BingBinCallback(action), token);
    }

    /**
     * populate recycle count data from json array to TextViews in grid
     *
     * @param array json array
     * @throws JSONException json exception
     */
    private void showRecycleCountData(JSONArray array) throws JSONException {
        int[] counts = new int[12];

        // copy json array data to an array
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            int type_trash = json.getInt("type_trash");
            int quantity = json.getInt("quantity");

            if (type_trash <= 12) {
                counts[type_trash - 1] = quantity;
            } // ignore 99 (other)
        }

        // show data
        TextView[] textViews = {ecopointCountText1, ecopointCountText2, ecopointCountText3,
                ecopointCountText4, ecopointCountText5, ecopointCountText6,
                ecopointCountText7, ecopointCountText8, ecopointCountText9,
                ecopointCountText10, ecopointCountText11, ecopointCountText12};
        activity.runOnUiThread(() -> {
            for (int i = 0; i < 12; i++) {
                if (textViews[i] != null) {
                    textViews[i].setText(String.valueOf(counts[i]));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
