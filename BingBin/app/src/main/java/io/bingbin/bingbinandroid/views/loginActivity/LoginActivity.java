package io.bingbin.bingbinandroid.views.loginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.catprogrammer.android.utils.AnimationUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.leakcanary.RefWatcher;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinCallback;
import io.bingbin.bingbinandroid.utils.BingBinCallbackAction;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.views.introActivity.IntroActivity;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
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
import studios.codelight.smartloginlibrary.util.UserUtil;

/**
 * Login activity.
 *
 * @author Junyang HE
 */
public class LoginActivity extends Activity implements SmartLoginCallbacks {

    private final int REGISTER = 233;

    @Inject
    BingBinHttp bbh;

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
    @BindView(R.id.hint_login_textview)
    TextView hintLoginTextview;

    @BindView(R.id.login_logo_layout)
    LinearLayoutCompat loginLogo;
    @BindView(R.id.login_bottom_grass)
    AppCompatImageView loginBottomGrass;
    @BindView(R.id.login_cardview)
    CardView loginCardview;
    @BindView(R.id.login_masterlayout)
    ConstraintLayout loginMasterlayout;
    @BindView(R.id.login_bottomimageslayout)
    ConstraintLayout loginBottomimageslayout;

    SmartUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;

    JSONObject userData = null;
    private String token = null;
    private boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        // config smart login
        config = new SmartLoginConfig(this, this);
        config.setFacebookAppId(getString(R.string.facebook_app_id));
        config.setFacebookPermissions(null); // use default
        config.setGoogleSignInClient(mGoogleSignInClient);

        // hide elements for animation
        loginLogo.setVisibility(View.INVISIBLE);
        loginCardview.setVisibility(View.INVISIBLE);
        loginBottomimageslayout.setVisibility(View.INVISIBLE);

        // First put logo on center
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(loginMasterlayout);
        constraintSet.connect(R.id.login_logo_layout, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(loginMasterlayout);

        loginMasterlayout.post(
            () -> AnimationUtil.revealView(loginBottomimageslayout, true,  // show grass
                    () -> AnimationUtil.revealView(loginLogo, true, // show logo
                            () -> {
                                currentUser = UserSessionManager.getCurrentUser(this);
                                if (currentUser != null) {
                                    Log.d("Smart Login", "Logged in user: " + currentUser.toString());
                                    toMainActivity();
                                } else {                // move up logo
                                // start constraint layout auto animation
                                TransitionManager.beginDelayedTransition(loginMasterlayout);
                                // clear connection just added to move up logo
                                constraintSet.clone(loginMasterlayout);
                                constraintSet.clear(R.id.login_logo_layout, ConstraintSet.BOTTOM);
                                constraintSet.applyTo(loginMasterlayout);

                                (new Handler()).postDelayed(
                                        () -> AnimationUtil.revealView(loginCardview,
                                                true, null),
                                        1000);
                                }
                            })
            )
        );


    }

    /**
     * Try find the logged in user with UserSessionManager, if found go to MainActivity
     */
    private void checkIsLoggedIn() {
        currentUser = UserSessionManager.getCurrentUser(this);
        if (currentUser != null) {
            Log.d("Smart Login", "Logged in user: " + currentUser.toString());
            toMainActivity();
        } else {
            AnimationUtil.revealView(loginCardview, true, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        } else if (requestCode == REGISTER) {
            if (resultCode == RESULT_CANCELED) {
                Log.d("register activity", "cancel");
                showLoader(false);
                return;
            }
            if (resultCode == RESULT_OK) {
                String resultToken = data.getStringExtra("token");
                Log.d("register activity", resultToken);
                isNewUser = true;
                token = resultToken;
                // login directly
                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                smartLogin.login(config);
            }
        }
    }

    /**
     * Call when social login success, doing normal login or register success
     *
     * If social login success, user will be a SmartGoogleUser or a SmartFacebookUser
     *     then, use social token to do a second bingbin login, and return a SmartUser with bingbin info
     *
     * If normal login or normal sign up finish, user will be a new SmartUser()
     *     then, will do a direct bingbin login, and return a SmartUser with bingbin info
     *
     * @param user  SmartGoogleUser, SmartFacebookUser or new SmartUser()
     */
    @Override
    public void onLoginSuccess(SmartUser user) {
        (new Thread(() -> {
            // if user normal register/login, token won't be null
            // if user social login, token will be null
            // so should send fb/google token to server to get the bingbintoken
            // if token not null, means user use normal login/register
            // can get user info by bingbintoken directly
            if (token == null) {
                String hint = "";
                try {
                    Response res = null;

                    // use bbh to synchronous get bingbintoken
                    if (user instanceof SmartGoogleUser) {
                        res = bbh.googleLogin(((SmartGoogleUser) user).getIdToken());
                        SmartLoginFactory.build(LoginType.Google).logout(this);
                    } else if (user instanceof SmartFacebookUser) {
                        res = bbh.facebookLogin(((SmartFacebookUser) user).getAccessToken().getToken());
                        SmartLoginFactory.build(LoginType.Facebook).logout(this);
                    }

                    if (res != null) {
                        // if request success
                        if (res.isSuccessful()) {
                            // parse json
                            JSONObject json = new JSONObject(res.body().string());
                            // if json doesn't have the 'error' key, means social token correct
                            if (!json.has("error")) {
                                // get the bingbintoken
                                token = json.getString("token");
                                // then bbh will ask server for user data from the bingbintoken
                                // and this thread should end here
                                // the thread of callback will start

                            } else { // if social token not correct
                                hint = "social token not valid";
                            }
                        } else { // if request failed
                            hint = getResources().getString(R.string.bingbinhttp_onresponsenotsuccess);
                        }
                    } else { // if request object is still null, means user object neither google nor fb, but token should not be null, so code won't reach here
                        hint = "what?";
                    }

                } catch (IOException e) { // bbh connexion error
                    e.printStackTrace();
                    hint = getResources().getString(R.string.bingbinhttp_onfailure);
                } catch (JSONException e) { // json parse error
                    e.printStackTrace();
                    hint = getResources().getString(R.string.bingbinhttp_onjsonparseerror);
                }

                // if no error code won't reach here
                // show error message and hide loader
                String finalHint = hint;
                runOnUiThread(() -> {
                    hintLoginTextview.setText(finalHint);
                    showLoader(false);
                });
            }

            if (token == null) return;

            Log.d("BingBin Login token", token);

            BingBinCallbackAction action = new BingBinCallbackAction() {
                @Override
                public void onFailure() {
                    runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onfailure));
                }

                @Override
                public void onResponseNotSuccess() {
                    runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onresponsenotsuccess));
                }

                @Override
                public void onJsonParseError() {
                    runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onjsonparseerror));
                }

                @Override
                public void onNotValid(String errorStr) {
                    runOnUiThread(() -> hintLoginTextview.setText(errorStr));
                }

                @Override
                public void onTokenNotValid(String errorStr) {
                    runOnUiThread(() -> hintLoginTextview.setText(errorStr));
                }

                @Override
                public void onValid(JSONObject json) throws JSONException{
                    // get user data from json obj and populate to SmartUser
                    SmartUser u = UserUtil.populateBingBinUser(json, token);
                    // set user session
                    UserSessionManager.setUserSession(LoginActivity.this, u);

                    runOnUiThread(() -> {
                        // hide loader, enable touch
                        showLoader(false);
                        //Toast.makeText(LoginActivity.this.getApplicationContext(), user.toString(), Toast.LENGTH_SHORT).show();
                        // go to main activity
                        checkIsLoggedIn();
                    });
                }

                @Override
                public void onAnyError() { }
            };

            bbh.getMyInfo(new BingBinCallback(action), token);

        })).start();

    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        // hide loader, enable touch
        showLoader(false);

        Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        return new SmartUser();
    }

    @Override
    public SmartUser doCustomSignup() {
        return new SmartUser();
    }

    private void toMainActivity() {
        Intent intent;
        if(isNewUser) {
            intent = new Intent(this, IntroActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.custom_signup_button, R.id.custom_signin_button, R.id.google_login_button, R.id.facebook_login_button})
    void setOnClick(View view) {
        showLoader(true);

        switch (view.getId()) {
            case R.id.custom_signup_button:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER);
                break;

            case R.id.custom_signin_button:
                // hide keyboard
                View currentFocus = LoginActivity.this.getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }
                emailEditText.clearFocus();
                passwordEditText.clearFocus();
                String email = emailEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
                if (StringUtils.isAnyBlank(email, pwd)) {
                    hintLoginTextview.setText(R.string.hint_login_no_email_pw);
                    showLoader(false);
                    return;
                }

                BingBinCallbackAction action = new BingBinCallbackAction() {
                    @Override
                    public void onFailure() {
                        runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onfailure));
                    }

                    @Override
                    public void onResponseNotSuccess() {
                        runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onresponsenotsuccess));
                    }

                    @Override
                    public void onJsonParseError() {
                        runOnUiThread(() -> hintLoginTextview.setText(R.string.bingbinhttp_onjsonparseerror));
                    }

                    @Override
                    public void onNotValid(String errorStr) {
                        runOnUiThread(() -> hintLoginTextview.setText(errorStr));
                    }

                    @Override
                    public void onTokenNotValid(String errorStr) {
                        runOnUiThread(() -> hintLoginTextview.setText(errorStr));
                    }

                    @Override
                    public void onValid(JSONObject json) throws JSONException {
                        userData = json;
                        token = userData.getString("token");
                        smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                        runOnUiThread(() -> smartLogin.login(config));
                    }

                    @Override
                    public void onAnyError() {
                        runOnUiThread(() -> showLoader(false));
                    }
                };

                bbh.login(new BingBinCallback(action),
                        emailEditText.getText().toString(), passwordEditText.getText().toString());

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

    private void showLoader(boolean show) {
        if (show) {
            loginProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            loginProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
