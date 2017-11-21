package io.bingbin.bingbinandroid;

import android.content.Intent;
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

import io.bingbin.bingbinandroid.tensorflow.ClassifierActivity;
import io.bingbin.bingbinandroid.utils.BottomNavigationViewHelper;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class MainActivity extends AppCompatActivity {

    private SmartUser currentUser;
    private boolean doubleBackToExitPressedOnce;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private MenuItem menuItem;

    // bottom navigation listner
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camara:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_home:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    // fragment viewpager listner
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

            // check is camera fragment selected
            // start Camera Activity
            if(position == 0) {
                startActivity(new Intent(MainActivity.this, ClassifierActivity.class));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = UserSessionManager.getCurrentUser(this);
        Log.d("SmartUser", currentUser.toString());
        if (currentUser instanceof SmartFacebookUser)
            Log.d("Smart Login", "Facebook ProfileName: " + ((SmartFacebookUser) currentUser).getProfileName());
        if (currentUser instanceof SmartGoogleUser)
            Log.d("Smart Login", "Google DisplayName: " + ((SmartGoogleUser) currentUser).getDisplayName());

        // init Fresco
        Fresco.initialize(this);

        // init viewpager
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(onPageChangeListner);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // init bottom navigation
        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

    public SmartUser getCurrentUser() {
        return currentUser;
    }
}
