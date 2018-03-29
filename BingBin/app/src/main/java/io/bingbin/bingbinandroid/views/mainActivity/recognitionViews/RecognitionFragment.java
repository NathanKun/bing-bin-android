package io.bingbin.bingbinandroid.views.mainActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.SwipeDirection;
import io.bingbin.bingbinandroid.utils.BingBinMainViewPager;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import io.bingbin.bingbinandroid.views.avatarActivity.AvatarActivity;
import io.bingbin.bingbinandroid.views.classifyActivity.CameraActivity;
import io.bingbin.bingbinandroid.views.classifyActivity.ClassifyActivity;
import io.bingbin.bingbinandroid.views.infoActivity.InfoActivity;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.EcoPointFragment;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.GetEcoPointFragment;

import static android.app.Activity.RESULT_OK;

/**
 * Main Fragment for Recognition Page of MainActivity
 *
 * @author Junyang HE
 */
public class RecognitionFragment extends Fragment {

    @SuppressWarnings("FieldCanBeLocal")
    private final int GALLERY_PICTURE = 233;
    private final int CLASSIFY = 6;
    private final int CHANGE_AVATAR = 6666;

    MainActivity activity;

    @BindView(R.id.recognition_viewpager)
    public BingBinMainViewPager viewPager;
    private ViewPagerAdapter adapter;

    private Unbinder unbinder;

    public RecognitionFragment() {
    }

    public static RecognitionFragment newInstance() {
        return new RecognitionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recognition, container, false);
        unbinder = ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();

        // init viewpager
        viewPager.addOnPageChangeListener(onPageChangeListner);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    // fragment viewpager listener
    private final ViewPager.OnPageChangeListener onPageChangeListner = (new ViewPager.OnPageChangeListener() {
        // 滚动过程中会不断触发
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        // 滚动结束后触发
        @Override
        public void onPageSelected(int position) {
            /*if (menuItem != null) {
                menuItem.setChecked(false);
            }*/
            if (position >= 3) { // when forth/fifth page, disable swiping left and right
                viewPager.setAllowedSwipeDirection(SwipeDirection.NONE);
            } else {
                /*menuItem = navigation.getMenu().getItem(position); // for navigation, max index is 2
                menuItem.setChecked(true);*/
                if (position == 2) { // when on third page, block swiping right
                    viewPager.setAllowedSwipeDirection(SwipeDirection.LEFT);
                } else { // else allow all direction swiping
                    viewPager.setAllowedSwipeDirection(SwipeDirection.ALL);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    });

    // ============
    // On results
    // ============
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // WelcomeFragment, Gallery btn, image selected, start ClassifyActivity
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            if (uri != null) {
                startClassifyActivity(uri);
            }
        } else if (requestCode == CLASSIFY) {
            if (resultCode == RESULT_OK) {
                Log.d("Classify End", "To trier fragment");
                int ecoPoint = data.getIntExtra("ecopoint", 0);
                if (ecoPoint == 0) {
                    Log.e("ecopoint gain", "ecopoint gain 0");
                }
                viewPager.setCurrentItem(3);
                ((GetEcoPointFragment) adapter.getRegisteredFragment(3)).setEcoPoint(ecoPoint);
            } else {
                Log.d("Classify End", "CANCEL");
            }
        } else if (requestCode == CHANGE_AVATAR){
            ((EcoPointFragment)adapter.getRegisteredFragment(0)).getMyInfoToUpdateUserAndPoints(activity.getCurrentUser().getToken());
        }
    }



    public void startClassifyActivity(Uri uri) {
        // call after gallery end
        Intent intent = new Intent(activity, ClassifyActivity.class);
        intent.putExtra("uri", uri);
        startActivityForResult(intent, CLASSIFY);
    }

    public void startCameraActivity() {
        startActivityForResult(new Intent(activity, CameraActivity.class), CLASSIFY);
    }

    public void startAvatarActivity() {
        startActivityForResult(new Intent(activity, AvatarActivity.class), CHANGE_AVATAR);
    }

    public void startInfoActivity() {
        startActivity(new Intent(activity, InfoActivity.class));
    }
}
