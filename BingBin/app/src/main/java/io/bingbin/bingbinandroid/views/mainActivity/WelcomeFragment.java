package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;


/**
 * Fragment contains button to gallery and button to CameraActivity.
 * This fragment doesn't contain any business logic of camera.
 * The real activity of camera is CameraActivity
 *
 * @author Junyang HE
 */
public class WelcomeFragment extends Fragment {

    @BindView(R.id.welcome_image_btn)
    Button welcomeImageBtn;
    @BindView(R.id.welcome_camera_btn)
    Button welcomeCameraBtn;
    @BindView(R.id.home_welcome_textview)
    TextView homeWelcomeTextview;
    @BindView(R.id.logout_btn)
    Button logoutBtn;

    private MainActivity activity;
    private Unbinder unbinder;

    public WelcomeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WelcomeFragment.
     */
    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getUserVisibleHint(). This value is by default true, so
        setUserVisibleHint(false);
        this.activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // button to start gallery
        welcomeImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, activity.GALLERY_PICTURE);
        });

        // button to start camera activity
        welcomeCameraBtn.setOnClickListener(view -> {
            activity.startClassifyActivity(""); // no uri, so ClassifyActivity will start CameraActivity
        });
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        homeWelcomeTextview.setText(String.format(activity.getResources().getString(R.string.home_homefragment), activity.getCurrentUser().getFirstName()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.logout_btn)
    void logoutOnClick(View view) {
        SmartUser currentUser = activity.getCurrentUser();
        SmartLogin smartLogin;
        if (currentUser != null) {
            if (currentUser instanceof SmartFacebookUser) {
                smartLogin = SmartLoginFactory.build(LoginType.Facebook);
            } else if (currentUser instanceof SmartGoogleUser) {
                smartLogin = SmartLoginFactory.build(LoginType.Google);
            } else {
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
            }
            boolean result = smartLogin.logout(activity);
            if (result) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            } else {
                Log.d("Smart Login", "Logout failed");
            }
        }
    }
}
