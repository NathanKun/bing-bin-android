package io.bingbin.bingbinandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

import io.bingbin.bingbinandroid.utils.BottomNavigationViewHelper;
import io.bingbin.bingbinandroid.utils.ViewPagerAdapter;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST_CAMERA = 2333;
    private final int CAMERA_ACTIVITY = 23333;
    protected final int GALLERY_PICTURE = 233;

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
                // back to home
                MainActivity.this.runOnUiThread(() -> {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> navigation.setSelectedItemId(R.id.navigation_home), 500);
                });
                startActivityForResult(new Intent(MainActivity.this, CameraPhotoActivity.class), CAMERA_ACTIVITY);
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

    public ViewPagerAdapter getAdapter() {
        return adapter;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            String path = data.getStringExtra("imgPath");
            path = path.replace("file:", "");
            ((HomeFragment) adapter.getRegisteredFragment(1)).recognitionFile(new File(path));
        } else if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK
                && null != data) {
            HomeFragment fgm = ((HomeFragment) adapter.getRegisteredFragment(1));
            Uri imgUri = data.getData();
            File imgFile = fgm.uriToFile(imgUri);
            fgm.recognitionFile(imgFile);
        }
    }
}
