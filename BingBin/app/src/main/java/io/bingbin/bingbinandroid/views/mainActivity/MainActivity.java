package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx;
import io.bingbin.bingbinandroid.views.classifyActivity.ClassifyActivity;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Main Activity.
 * Invoke after login.
 *
 * @author Junyang HE
 */
public class MainActivity extends AppCompatActivity {

    protected final int GALLERY_PICTURE = 233;
    private final int CLASSIFY = 6;
    private final int CLASSIFY_END_TRIER = 66;
    private final int CLASSIFY_END_RECYCLER = 666;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    private SmartUser currentUser;
    private MenuItem menuItem;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUser = UserSessionManager.getCurrentUser(this);
        Log.d("Main activity user", currentUser.toString());

        // init Fresco
        if (!Fresco.hasBeenInitialized())
            Fresco.initialize(this);

        // init viewpager
        viewPager.addOnPageChangeListener(onPageChangeListner);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // init bottom navigation
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_welcome);
    }

    // back twice to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    // ============
    // On results
    // ============

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Classify End", String.valueOf(requestCode));
        // WelcomeFragment, Gallery btn, image selected, start ClassifyActivity
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            assert uri != null;
            startClassifyActivity(uri.toString());
        } else if(requestCode == CLASSIFY){
            if(resultCode == CLASSIFY_END_RECYCLER) {
                Log.d("Classify End", "To recycler fragment");
            } else if(resultCode == CLASSIFY_END_TRIER){
                Log.d("Classify End", "To trier fragment");
            }
        }
    }

    // ============
    // Listeners
    // ============

    // bottom navigation listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ecopoint:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_welcome:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_rank:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    // fragment viewpager listener
    private ViewPager.OnPageChangeListener onPageChangeListner = (new ViewPager.OnPageChangeListener() {
        // 滚动过程中会不断触发
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        // 滚动结束后触发
        @Override
        public void onPageSelected(int position) {
            if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }
            menuItem = navigation.getMenu().getItem(position);
            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    });

    // ============
    // Getters
    // ============

    public SmartUser getCurrentUser() {
        return currentUser;
    }


    // ============
    // Methods
    // ============

    public void startClassifyActivity(String uri) {
        // start camera activity, with no uri, so ClassifyActivity will start camera activity.
        Intent intent = new Intent(this, ClassifyActivity.class);
        intent.putExtra("uri", uri);
        startActivityForResult(intent, CLASSIFY);
    }
}
