package io.bingbin.bingbinandroid.views.loginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.utils.NetComponent;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
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
    private final int CANCEL = 2333;
    private final int SUCCESS = 23333;

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


    SmartUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;

    JSONObject userData = null;
    private String token = null;

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
            toMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (smartLogin != null) {
            smartLogin.onActivityResult(requestCode, resultCode, data, config);
        } else if (requestCode == REGISTER) {
            if (resultCode == CANCEL) {
                Log.d("register activity ended", "cancel");
                showLoader(false);
                return;
            }
            if (resultCode == SUCCESS) {
                Log.d("register activity ended", "success");
                showLoader(false);
                String resultToken = data.getStringExtra("token");
                Log.d("register activity ended", resultToken);
                token = resultToken;
            }
        }
    }

    /**
     * on social login success, login check or register success
     * @param user
     */
    @Override
    public void onLoginSuccess(SmartUser user) {
        (new Thread(() -> {
            // if user normal register/login, token won't be null
            // if user social login, token will be null
            // so should send fb/google token to server to get the bingbintoken
            // if token not null, means user use normal login/register
            // can get user info by bingbintoken directly
            if(token == null) {
                String hint = "";
                try {
                    Response res = null;

                    // use bbh to synchronous get bingbintoken
                    if(user instanceof SmartGoogleUser) {
                        res = bbh.googleLogin(((SmartGoogleUser) user).getIdToken());
                    } else if(user instanceof SmartFacebookUser) {
                        res = bbh.facebookLogin(((SmartFacebookUser) user).getAccessToken().getToken());
                    }

                    if(res != null) {
                        // if request success
                        if(res.isSuccessful()) {
                            // parse json
                            JSONObject json = new JSONObject(res.body().string());
                            // if json doesn't have the 'error' key, means social token correct
                            if(!json.has("error")) {
                                // get the bingbintoken
                                String resultToken = json.getString("token");
                                // ask server for user data from the bingbintoken
                                // and this thread should end here
                                // the thread of callback will start
                                token = resultToken;
                            } else { // if social token not correct
                                hint = "social token not valid";
                            }
                        } else { // if request failed
                            hint = "Request not success";
                        }
                    } else { // if request object is still null, means user object neither google nor fb, but token should not be null, so code won't reach here
                        hint = "what?";
                    }

                }catch (IOException e) { // bbh connexion error
                    e.printStackTrace();
                    hint = "Erreur de connextion";
                } catch (JSONException e) { // json parse error
                    e.printStackTrace();
                    hint = "Json error";
                }

                // if no error code won't reach here
                // show error message and hide loader
                String finalHint = hint;
                runOnUiThread(() -> {
                    hintLoginTextview.setText(finalHint);
                    showLoader(false);
                });
            }

            if(token == null) return;

            // callback for bbh.getMyInfo
            Callback cb = new Callback() {
                private String token;

                // pass the bingbintoken in to inner class
                public Callback init (String token) {
                    this.token = token;
                    return this;
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> hintLoginTextview.setText("Erreur de connexion"));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String hint = "";
                    try{
                        // get json obj from response body
                        JSONObject json = new JSONObject(response.body().string());
                        // if token is valid
                        if(json.getBoolean("valid")) {
                            // get user data from json obj and populate to SmartUser
                            SmartUser u = UserUtil.populateBingBinUser(json.getJSONObject("data"), token);
                            // set user session
                            UserSessionManager.setUserSession(LoginActivity.this, u);

                            runOnUiThread(() -> {
                                // hide loader, enable touch
                                showLoader(false);
                                Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                                // go to main activity
                                refreshLayout();
                            });
                        } else { // if token is not valid
                            hint = "BingBinToken not valid";
                        }
                    } catch (JSONException e) { // if parse json error
                        hint = "Json parse error";
                        e.printStackTrace();
                    }

                    // if any error, show hint and hide loader
                    // if no error, won't reach this
                    String finalHint = hint;
                    runOnUiThread(() -> {
                        hintLoginTextview.setText(finalHint);
                        showLoader(false);
                    });
                }
            }.init(token); // made sure token not null before

            bbh.getMyInfo(cb, token);
        })).start();

    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        // hide loader, enable touch
        showLoader(false);

        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, MainActivity.class);
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
                String email = emailEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
                if(StringUtils.isAnyBlank(email, pwd)) {
                    hintLoginTextview.setText("Veuillez entrer votre email et votre mot de pass");
                    showLoader(false);
                    return;
                }

                Callback callback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            hintLoginTextview.setText("Erreur de connexion");
                            showLoader(false);
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()){
                            runOnUiThread(() -> hintLoginTextview.setText("Request not success"));
                        } else {
                            String hint = "";
                            String resultToken = "";

                            // check login result
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                if (json.getBoolean("valid")) {
                                    resultToken = json.getString("token");
                                    userData = json.getJSONObject("data");
                                } else {
                                    hint = json.getString("token error");
                                }
                            } catch (JSONException e) {
                                hint = "Response JSON error";
                                e.printStackTrace();
                            }

                            // if login failed, end
                            if (StringUtils.isNotBlank(hint)) {
                                String finalHint = hint;
                                runOnUiThread(() -> {
                                    hintLoginTextview.setText(finalHint);
                                    showLoader(false);
                                });
                                return;
                            }

                            // if login success
                            String finalResultToken = resultToken;
                            runOnUiThread(() -> {
                                token = finalResultToken;
                                smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                                smartLogin.login(config);
                            });
                        }
                    }
                };
                bbh.login(callback, emailEditText.getText().toString(), passwordEditText.getText().toString());
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
