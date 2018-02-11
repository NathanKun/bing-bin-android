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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
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
        } else if (requestCode == REGISTER) {
            if (resultCode == CANCEL) {
                Log.d("register activity ended", "cancel");
                showLoader(false);
                return;
            }
            if (resultCode == SUCCESS) {
                Log.d("register activity ended", "success");
                showLoader(false);
                String token = data.getStringExtra("token");
                Log.d("register activity ended", token);
                // TODO: Login by token
            }
        }
    }

    @Override
    public void onLoginSuccess(SmartUser user) {
        // hide loader, enable touch
        showLoader(false);

        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        refreshLayout();
    }

    @Override
    public void onLoginFailure(SmartLoginException e) {
        // hide loader, enable touch
        showLoader(false);

        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SmartUser doCustomLogin() {
        try {
            return UserUtil.populateBingBinUser(userData);
        } catch (JSONException e) {
            e.printStackTrace();
            return new SmartUser();
        }
    }

    @Override
    public SmartUser doCustomSignup() {
        return doCustomLogin();
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


                BingBinHttp bbh = new BingBinHttp();
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
                            String token = "";

                            // check login result
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                if (json.getBoolean("valid")) {
                                    token = json.getString("token");
                                    userData = json.getJSONObject("data");
                                } else {
                                    hint = json.getString("error");
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
                            runOnUiThread(() -> {
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
