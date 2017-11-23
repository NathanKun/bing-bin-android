package io.bingbin.bingbinandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;

import java.io.File;

import io.bingbin.bingbinandroid.utils.BottomNavigationViewHelper;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST_CAMERA = 2333;

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

            // check is camera nav button selected
            // start Camera Activity
            if (position == 0) {
                /*startActivity(new Intent(MainActivity.this, ClassifierActivity.class));
                // back to home
                MainActivity.this.runOnUiThread(() -> {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> navigation.setSelectedItemId(R.id.navigation_home), 500);
                });*/
                startCameraFragment();
                //startActivity(new Intent(MainActivity.this, CameraFragmentMainActivity.class));
            } else {
                stopCameraFragment();
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

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

    private void startCameraFragment() {
        // check and request permission
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CAMERA);
            return;
        }
        CameraFragment cameraFragment = CameraFragment.newInstance(
                new Configuration.Builder().setMediaAction(Configuration.MEDIA_ACTION_PHOTO).build());

        // record button
        findViewById(R.id.record_button).setOnClickListener((view -> cameraFragment.takePhotoOrCaptureVideo(
                new CameraFragmentResultAdapter() {
                    @Override
                    public void onVideoRecorded(String filePath) {
                        //called when the video record is finished and saved
                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {
                        //called when the photo is taken and saved
                        navigation.setSelectedItemId(R.id.navigation_home);
                        ((HomeFragment) adapter.getRegisteredFragment(1))
                                .recognitionFile(new File(filePath));
                    }
                },
                null, null)));

        // flash button
        findViewById(R.id.flash_switch_view).setOnClickListener((view -> {
            cameraFragment.toggleFlashMode();
        }));


        findViewById(R.id.cameraLayout).setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cameraBlank, cameraFragment, "CAMERA_REAL_FRAGMENT")
                .commit();
    }


    private void stopCameraFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CAMERA_REAL_FRAGMENT");
        if (fragment != null/* && fragment.isVisible()*/) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            findViewById(R.id.cameraLayout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraFragment();
                } else {
                    // permission denied
                    Toast.makeText(this, "Permission of camera and storage is needed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
