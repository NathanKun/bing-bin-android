package io.bingbin.bingbinandroid.views.mainActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.views.BottomNavigationViewEx;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import io.bingbin.bingbinandroid.views.mainActivity.recognitionViews.RecognitionFragment;
import io.bingbin.bingbinandroid.views.webActivity.WebActivity;
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

    @BindView(R.id.main_navigation)
    BottomNavigationViewEx navigation;
    @BindView(R.id.main_progress_bar)
    ProgressBar mainProgressBar;

    private SmartUser currentUser;
    private boolean doubleBackToExitPressedOnce;
    private RecognitionFragment recognitionFragment;

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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        recognitionFragment = RecognitionFragment.newInstance();
        transaction.replace(R.id.main_framelayout, recognitionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // back twice to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            Intent intent = new Intent("finish_web_activity");
            sendBroadcast(intent);

            // kill WebActivity
            /*ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            if(am != null) {
                List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo info : processes) {
                    if(info.processName.equals("io.bingbin.bingbinandroid:webview")) {
                        Process.killProcess(info.pid);
                    }
                }
            }*/
            finish();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this.getApplicationContext(), "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            handler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
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
                navigation.getMenu().getItem(1).setChecked(true);
                navigation.getMenu().getItem(0).setChecked(false);
                startActivity(makeWebActivityIntent("event"));
                return true;
            case R.id.navigation_recognition:
                recognitionFragment.toWelcomeFragment();
                return true;
            case R.id.navigation_forum:
                navigation.getMenu().getItem(1).setChecked(true);
                navigation.getMenu().getItem(2).setChecked(false);
                startActivity(makeWebActivityIntent("forum"));
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

    private Intent makeWebActivityIntent(String toPage) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("token", currentUser.getToken());
        intent.putExtra("toPage", toPage);
        return intent;
    }

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
