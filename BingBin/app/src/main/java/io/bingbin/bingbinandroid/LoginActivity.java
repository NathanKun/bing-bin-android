package io.bingbin.bingbinandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

/**
 * Login activity.
 *
 * @author Junyang HE
 */
public class LoginActivity extends Activity implements SmartLoginCallbacks {

    @BindView(R.id.email_edittext)
    EditText emailEditText;
    @BindView(R.id.password_edittext)
    EditText passwordEditText;
    @BindView(R.id.custom_signin_button)
    Button customSigninButton;
    @BindView(R.id.custom_signup_button)
    Button customSignupButton;
    @BindView(R.id.facebook_login_button)
    Button facebookLoginButton;
    @BindView(R.id.google_login_button)
    Button googleLoginButton;
    @BindView(R.id.login_progress_bar)
    ProgressBar loginProgressBar;


    SmartUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;

    LoginHandler myHandler = new LoginHandler(this);
    static class LoginHandler extends Handler {
        private final WeakReference<LoginActivity> mTarget;

        LoginHandler(LoginActivity target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity target = mTarget.get();
            if (target != null) {
                target.finishLogin();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // config smart login
        config = new SmartLoginConfig(this, this);
        config.setFacebookAppId(getString(R.string.facebook_app_id));
        config.setFacebookPermissions(null); // use default
        config.setGoogleSignInClient(mGoogleSignInClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = UserSessionManager.getCurrentUser(this);
        refreshLayout();
    }

    private void refreshLayout() {
        currentUser = UserSessionManager.getCurrentUser(this);
        if (currentUser != null) {
            Log.d("Smart Login", "Logged in user: " + currentUser.toString());
            if (currentUser instanceof SmartFacebookUser)
                Log.d("Smart Login", "Facebook ProfileName: " + ((SmartFacebookUser) currentUser).getProfileName());
            if (currentUser instanceof SmartGoogleUser)
                Log.d("Smart Login", "Google DisplayName: " + ((SmartGoogleUser) currentUser).getDisplayName());

            toMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        }
    }
    @Override
    public void onLoginSuccess(SmartUser user) {
        // hide loader, enable touch
        loginProgressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        // hide loader, enable touch
        loginProgressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        // TODO: other setters

        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        return doCustomLogin();
    }

    private void finishLogin() {
        smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
        smartLogin.login(config);
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.custom_signup_button, R.id.custom_signin_button, R.id.google_login_button, R.id.facebook_login_button})
    public void setOnClick(View view) {
        loginProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        switch (view.getId()){
            case R.id.custom_signup_button:
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.login(config);
                break;
            case R.id.custom_signin_button:
                BingBinHttp bbh = new BingBinHttp();
                bbh.test(myHandler);
                break;
            case R.id.google_login_button:
                // Perform Google login
                smartLogin = SmartLoginFactory.build(LoginType.Google);
                smartLogin.login(config);
                break;
            case R.id.facebook_login_button:
                // Perform Facebook login
                smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                smartLogin.login(config);
                break;
        }
    }

}
