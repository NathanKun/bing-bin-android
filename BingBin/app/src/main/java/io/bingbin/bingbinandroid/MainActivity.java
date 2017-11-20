package io.bingbin.bingbinandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import io.bingbin.bingbinandroid.utils.BottomNavigationViewHelper;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private MenuItem menuItem;

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

    private ViewPager.OnPageChangeListener onPageChangeListner = (new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

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

    private void setupViewPager(ViewPager viewPager) {    // 获取 View 并加入 List
        List<View> views = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);

        views.add(inflater.inflate(R.layout.fragment_home, null));
        views.add(inflater.inflate(R.layout.fragment_camera, null));
        views.add(inflater.inflate(R.layout.fragment_user, null));

        ViewPagerAdapter adapter = new ViewPagerAdapter(views, this);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        SmartUser currentUser = (SmartUser)intent.getSerializableExtra("user");
        Log.d("SmartUser", currentUser.toString());
        if (currentUser instanceof SmartFacebookUser)
            Log.d("Smart Login", "Facebook ProfileName: " + ((SmartFacebookUser) currentUser).getProfileName());
        if (currentUser instanceof SmartGoogleUser)
            Log.d("Smart Login", "Google DisplayName: " + ((SmartGoogleUser) currentUser).getDisplayName());

        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(onPageChangeListner);
        setupViewPager(viewPager);

        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        // init Fresco
        Fresco.initialize(this);
    }

}
