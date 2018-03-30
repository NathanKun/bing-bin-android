package io.bingbin.bingbinandroid.views.mainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.SwipeDirection;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.utils.CustomSwipeDirectionViewPager;
import io.bingbin.bingbinandroid.utils.MainViewPagerAdapter;
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartUser;

/**
 * Main Activity.
 * Invoke after login.
 *
 * @author Junyang HE
 */
@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    public final Handler handler = new Handler();

    @Inject
    public BingBinHttp bbh;

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;
    @BindView(R.id.main_progress_bar)
    ProgressBar mainProgressBar;
    @BindView(R.id.main_viewpager)
    CustomSwipeDirectionViewPager viewPager;

    private SmartUser currentUser;
    private boolean doubleBackToExitPressedOnce;

    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        currentUser = UserSessionManager.getCurrentUser(this);
        Log.d("Main activity user", currentUser.toString());

        // init bottom navigation
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        navigation.setSelectedItemId(R.id.navigation_recognition);

        // init viewpager
        viewPager.setAllowedSwipeDirection(SwipeDirection.NONE);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1, false);
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
        Toast.makeText(this.getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        handler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    // ============
    // Listeners
    // ============

    // bottom navigation listener
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_event:
                viewPager.setCurrentItem(0, false);
                return true;
            case R.id.navigation_recognition:
                viewPager.setCurrentItem(1, false);
                return true;
            case R.id.navigation_forum:
                viewPager.setCurrentItem(2, false);
                return true;
        }
        return false;
    };

    // ============
    // Getters
    // ============

    public SmartUser getCurrentUser() {
        return currentUser;
    }


    // ============
    // Methods
    // ============

    public void showLoader(boolean show) {
        if (show) {
            mainProgressBar.setVisibility(View.VISIBLE);
            enableInput(false);
        } else {
            mainProgressBar.setVisibility(View.GONE);
            enableInput(true);
        }
    }

    public void enableInput(boolean enable) {
        if (enable) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void backToLoginActivity() {
        runOnUiThread(() -> {
            Toast.makeText(this.getApplicationContext(),
                    "Session expiré", Toast.LENGTH_SHORT).show();

            removeCurrentUserFromSession();

            handler.postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        });
    }

    public void removeCurrentUserFromSession() {
        if (currentUser != null) {
            SmartLoginFactory.build(LoginType.CustomLogin).logout(this);
        }
    }
}
