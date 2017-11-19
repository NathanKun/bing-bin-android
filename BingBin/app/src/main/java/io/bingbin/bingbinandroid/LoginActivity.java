package io.bingbin.bingbinandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;
import java.util.Arrays;

import io.bingbin.bingbinandroid.tensorflow.env.Logger;
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
 * Created by NathanKun on 2017/11/18.
 * Login activity.
 */
public class LoginActivity extends Activity implements SmartLoginCallbacks {

    private Button facebookLoginButton, googleLoginButton, customSigninButton, customSignupButton, logoutButton;
    private EditText emailEditText, passwordEditText;
    SmartUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();

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
                Log.d("Smart Login", "Facebook ProfileName: " + ((SmartFacebookUser)currentUser).getProfileName());
            if (currentUser instanceof  SmartGoogleUser)
                Log.d("Smart Login", "Facebook ProfileName: " + ((SmartGoogleUser)currentUser).getDisplayName());
            facebookLoginButton.setVisibility(View.GONE);
            googleLoginButton.setVisibility(View.GONE);
            customSigninButton.setVisibility(View.GONE);
            customSignupButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            passwordEditText.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            facebookLoginButton.setVisibility(View.VISIBLE);
            googleLoginButton.setVisibility(View.VISIBLE);
            customSigninButton.setVisibility(View.VISIBLE);
            customSignupButton.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        }
    }

    private void setListeners() {
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Facebook login
                smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                smartLogin.login(config);
            }
        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Google login
                smartLogin = SmartLoginFactory.build(LoginType.Google);
                smartLogin.login(config);
            }
        });

        customSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign in
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.login(config);
            }
        });

        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign up
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.signup(config);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    if (currentUser instanceof SmartFacebookUser) {
                        smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                    } else if(currentUser instanceof SmartGoogleUser) {
                        smartLogin = SmartLoginFactory.build(LoginType.Google);
                    } else {
                        smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                    }
                    boolean result = smartLogin.logout(LoginActivity.this);
                    if (result) {
                        refreshLayout();
                        Toast.makeText(LoginActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void bindViews() {
        facebookLoginButton = findViewById(R.id.facebook_login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        customSigninButton = findViewById(R.id.custom_signin_button);
        customSignupButton = findViewById(R.id.custom_signup_button);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        logoutButton = findViewById(R.id.logout_button);
    }

    @Override
    public void onLoginSuccess(SmartUser user) {
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        SmartUser user = new SmartUser();
        user.setEmail(emailEditText.getText().toString());
        return user;
    }
}
