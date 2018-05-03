package io.bingbin.bingbinandroid.views.mainActivity.recognitionViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.catprogrammer.android.utils.AnimationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
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
    @BindView(R.id.ecopoint_count_swiperefresh)
    SwipeRefreshLayout ecopointCountSwiperefresh;

    @SuppressWarnings("ConstantConditions")
    private AppCompatImageView[] imageviews;

    private final int[] bigImgIds = {R.drawable.catg_1_plastic, R.drawable.catg_2_metal, R.drawable.catg_3_cardboard,
            R.drawable.catg_4_paper, R.drawable.catg_5_glass, R.drawable.catg_6_food,
            R.drawable.catg_13_cigarette, R.drawable.catg_8_cumbersome, R.drawable.catg_9_electronic,
            R.drawable.catg_10_battery, R.drawable.catg_11_clothe, R.drawable.catg_14_human};

    private MainActivity activity;
    private RecognitionFragment mainFragment;
    private Unbinder unbinder;
    private SimpleAdapter countDataListAdapter;
    private final ArrayList<Map<String, Object>> recycleHistoryDataToShow = new ArrayList<>();
    private boolean isShowingGrid;
    private int[] recycleCountDataToShow;
    private Bitmap avatarBitmap;

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

        mainFragment = (RecognitionFragment) getParentFragment();
        return view;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        SmartUser user = activity.getCurrentUser();

        // hide gridlayout, listview
        ecopointCountSwiperefresh.setVisibility(View.INVISIBLE);
        ecopointHistorySwiperefresh.setVisibility(View.GONE);


        // ------ show user info ------

        // generate avatar
        avatarBitmap = AvatarHelper.generateAvatar(activity, user.getRabbit(), user.getLeaf(), 2);
        Glide.with(this)
                .load(avatarBitmap)
                .into(ecopointAvatarImageview);

        // show username
        ecopointUsernameTextview.setText(user.getFirstName());

        // show eco point and sun point
        ecopointEcopointTextview.setText(String.valueOf(user.getEcoPoint()));
        ecopointSunpointTextview.setText(String.valueOf(user.getSunPoint()));


        // ------ grid view ------

        // add swipe down refresh listener, call getRecycleByCategoryData() when swipe down
        ecopointCountSwiperefresh.setOnRefreshListener(() -> getRecycleByCategoryData(user.getToken()));

        // adjust grid icon size
        ecopointCountSwiperefresh.post(() -> {
            int maxHeight = ecopointCountSwiperefresh.getHeight();
            int numberHeight = ecopointCountText1.getHeight();
            int targetHeight = (maxHeight - 4 * (numberHeight + 16)) / 4;
            int maxWidth = ecopointCountSwiperefresh.getWidth();
            int targetWidth = (maxWidth - 4 * 16) / 3;
            int target = targetHeight < targetWidth ? targetHeight : targetWidth;

            int width = 200;
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                width = size.x / 7;
            }

            target = target > width ? width : target;

            Log.d("Ecopoint grid newHeight", String.valueOf(target));

            imageviews = new AppCompatImageView[]{ecopointIconImg1, ecopointIconImg2, ecopointIconImg3,
                    ecopointIconImg4, ecopointIconImg5, ecopointIconImg6,
                    ecopointIconImg7, ecopointIconImg8, ecopointIconImg9,
                    ecopointIconImg10, ecopointIconImg11, ecopointIconImg12
            };

            for (int i = 0; i < imageviews.length; i++) {
                AppCompatImageView iv = imageviews[i];
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.height = target;
                params.width = target;
                iv.setLayoutParams(params);

                Glide.with(this)
                        .load(bigImgIds[i])
                        .into(iv);
            }
        });


        // ------ listview ------

        // add swipe down refresh listener, call getRecycleHistoryData() when swipe down
        ecopointHistorySwiperefresh.setOnRefreshListener(() -> getRecycleHistoryData(user.getToken()));

        // add adapter to listview, link recycleHistoryDataToShow to adapter
        countDataListAdapter = new SimpleAdapter(mainFragment.getContext(), recycleHistoryDataToShow, R.layout.listview_historylist,
                KEYS, IDS);
        ecopointHistorylistView.setAdapter(countDataListAdapter);


        // ------ listener to switch between grid and list ------

        ecopointEcopointTextview.setOnClickListener((v) -> {
            isShowingGrid = !isShowingGrid;
            switchBetweenGridAndList(isShowingGrid);
        });


        // ------ listener to change avatar ------
        ecopointAvatarImageview.setOnClickListener((view -> mainFragment.startAvatarActivity()));


        // ------ get data and show ------

        if (b != null) {
            // recycle history list
            ArrayList list = (ArrayList) b.getSerializable("recycleHistoryData");
            if (list != null) {
                for (Object m : list) {
                    recycleHistoryDataToShow.add((Map<String, Object>) m);
                }
                countDataListAdapter.notifyDataSetChanged();
                Log.d("EcoPointFragment bundle", "History list size = " + list.size());

            } else {
                getRecycleHistoryData(user.getToken());
            }

            // recycle count grid
            recycleCountDataToShow = b.getIntArray("recycleCountData");
            if (recycleCountDataToShow != null && recycleCountDataToShow.length != 0) {
                showRecycleCountData();
                Log.d("EcoPointFragment bundle", "Count array size = " + recycleCountDataToShow.length);
            } else {
                getRecycleByCategoryData(user.getToken());
            }
        } else {
            getRecycleHistoryData(user.getToken());
            getRecycleByCategoryData(user.getToken());
        }

        getMyInfoToUpdateUserAndPoints(user.getToken());


        // show gridlayout
        ecopointCountSwiperefresh.setVisibility(View.VISIBLE);
        isShowingGrid = true;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle b) {
        super.onSaveInstanceState(b);
        b.putSerializable("recycleHistoryData", recycleHistoryDataToShow);
        b.putIntArray("recycleCountData", recycleCountDataToShow);
    }

    /**
     * Convenient method to switch between grid and list
     *
     * @param showGrid is going to show Grid and hide List
     */
    private void switchBetweenGridAndList(boolean showGrid) {
        activity.enableInput(false);
        AnimationUtil.revealView(ecopointCountSwiperefresh, showGrid, 500);
        AnimationUtil.revealView(ecopointHistorySwiperefresh, !showGrid, 500);
        activity.handler.postDelayed(() -> activity.enableInput(true), 750);
    }

    /**
     * get user info, update user session and info showing
     *
     * @param token BingBinToken of the current user
     */
    public void getMyInfoToUpdateUserAndPoints(String token) {
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
            public void onAnyError() {
            }
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
                activity.runOnUiThread(() -> {
                    if (ecopointHistorySwiperefresh != null) {
                        ecopointHistorySwiperefresh.setRefreshing(false);
                    }
                });
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

        // updata data set
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

        // show data in list
        countDataListAdapter.notifyDataSetChanged();


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
                transformRecycleCountArrayToData(historyArray);
            }

            @Override
            public void onAnyError() {
                activity.runOnUiThread(() -> {
                    if (ecopointCountSwiperefresh != null) {
                        ecopointCountSwiperefresh.setRefreshing(false);
                    }
                });
            }
        };

        ecopointCountSwiperefresh.setRefreshing(true);
        activity.bbh.getMyRecycleCounts(new BingBinCallback(action), token);
    }

    /**
     * populate recycle count data from json array to TextViews in grid
     *
     * @param array json array
     * @throws JSONException json exception
     */
    private void transformRecycleCountArrayToData(JSONArray array) throws JSONException {
        recycleCountDataToShow = new int[14];

        // copy json array data to an array
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            int type_trash = json.getInt("type_trash");
            int quantity = json.getInt("quantity");

            if (type_trash <= 14) {
                recycleCountDataToShow[type_trash - 1] = quantity;
            } // ignore 99 (other)
        }

        // show data
        showRecycleCountData();
    }

    private void showRecycleCountData() {
        TextView[] textViews = {ecopointCountText1, ecopointCountText2, ecopointCountText3,
                ecopointCountText4, ecopointCountText5, ecopointCountText6,
                ecopointCountText7, ecopointCountText8, ecopointCountText9,
                ecopointCountText10, ecopointCountText11, ecopointCountText12};
        activity.runOnUiThread(() -> {
            for (int i = 0; i < 12; i++) {
                if (textViews[i] != null) {
                    if (i == 6) { // text7 is cigarette but id = 13
                        textViews[i].setText(String.valueOf(recycleCountDataToShow[12]));
                    } else if (i == 11) { // text12 is cigarette but id = 14
                        textViews[i].setText(String.valueOf(recycleCountDataToShow[13]));
                    } else {
                        textViews[i].setText(String.valueOf(recycleCountDataToShow[i]));
                    }
                }
            }
            if (ecopointCountSwiperefresh != null) {
                ecopointCountSwiperefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        avatarBitmap.recycle();
    }
}
