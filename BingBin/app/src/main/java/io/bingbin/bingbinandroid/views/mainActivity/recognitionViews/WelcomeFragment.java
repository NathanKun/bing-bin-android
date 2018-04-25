package io.bingbin.bingbinandroid.views.mainActivity.recognitionViews;

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

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.views.introActivity.IntroActivity;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;


/**
 * Welcome activity
 *
 * @author Junyang HE
 */
public class WelcomeFragment extends Fragment {

    @SuppressWarnings("FieldCanBeLocal")
    private final int GALLERY_PICTURE = 233;

    @BindView(R.id.welcome_image_btn)
    Button welcomeImageBtn;
    @BindView(R.id.welcome_camera_btn)
    Button welcomeCameraBtn;
    @BindView(R.id.home_welcome_textview)
    TextView homeWelcomeTextview;
    @BindView(R.id.welcome_fab)
    FloatingActionsMenu welcomeFab;

    private MainActivity activity;
    private RecognitionFragment mainFragment;
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
        mainFragment = (RecognitionFragment) getParentFragment();
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
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        homeWelcomeTextview.setText(String.format(activity.getResources().getString(R.string.home_homefragment), activity.getCurrentUser().getFirstName()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.welcome_fab_logout)
    void logoutOnClick() {
        welcomeFab.toggle();
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
                Intent broadcast = new Intent("finish_web_activity");
                activity.sendBroadcast(broadcast);
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            } else {
                Log.d("Smart Login", "Logout failed");
            }
        }
    }

    @OnClick(R.id.welcome_fab_avatar)
    void changeAvatarOnClick() {
        welcomeFab.toggle();
        mainFragment.startAvatarActivity();
    }

    @OnClick(R.id.welcome_fab_info)
    void infoOnClick() {
        welcomeFab.toggle();
        mainFragment.startInfoActivity();
    }

    @OnClick(R.id.welcome_fab_tutorial)
    void tutorialOnClick() {
        welcomeFab.toggle();
        Intent intent = new Intent(activity, IntroActivity.class);
        startActivity(intent);
        activity.finish();
    }


    // button to start gallery
    @OnClick(R.id.welcome_image_btn)
    void imageBtnOnClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mainFragment.startActivityForResult(intent, GALLERY_PICTURE);
    }

    // button to start camera activity
    @OnClick(R.id.welcome_camera_btn)
    void cameraBtnOnClick() {
        mainFragment.startCameraActivity();
    }
}
