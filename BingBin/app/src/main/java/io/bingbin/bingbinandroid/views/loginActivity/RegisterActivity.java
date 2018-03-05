package io.bingbin.bingbinandroid.views.loginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinCallback;
import io.bingbin.bingbinandroid.utils.BingBinCallbackAction;
import io.bingbin.bingbinandroid.utils.BingBinHttp;

public class RegisterActivity extends AppCompatActivity {

    @Inject
    BingBinHttp bbh;

    @BindView(R.id.email_register_edittext)
    EditText emailRegisterEdittext;
    @BindView(R.id.name_register_edittext)
    EditText nameRegisterEdittext;
    @BindView(R.id.password_registger_edittext)
    EditText passwordRegistgerEdittext;
    @BindView(R.id.password2_registger_edittext)
    EditText password2RegistgerEdittext;
    @BindView(R.id.hint_register_textView)
    TextView hintRegisterTextView;
    @BindView(R.id.register_progress_bar)
    ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_register_button)
    void buttonOnClick(View view){
        String email = emailRegisterEdittext.getText().toString();
        String firstname = nameRegisterEdittext.getText().toString();
        String pwd = passwordRegistgerEdittext.getText().toString();
        String pwd2 = password2RegistgerEdittext.getText().toString();
        if(StringUtils.isAnyBlank(email, firstname, pwd, pwd2)) {
            hintRegisterTextView.setText(R.string.hint_register_missing_fields);
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            hintRegisterTextView.setText(R.string.hint_register_email_not_correct);
            return;
        }
        if(!pwd.equals(pwd2)) {
            hintRegisterTextView.setText(R.string.hint_register_password_different);
            return;
        }
        if(pwd.length() < 6) {
            hintRegisterTextView.setText(R.string.hint_register_password_short);
            return;
        }
        showLoader(true);

        // BingBinHttp register
        // callback success => end activity and send user, loginProgressBar GONE
        // callback error => setText, loginProgressBar GONE
        BingBinCallbackAction action = new BingBinCallbackAction() {
            @Override
            public void onFailure() {
                runOnUiThread(() -> hintRegisterTextView.setText(R.string.bingbinhttp_onfailure));
            }

            @Override
            public void onResponseNotSuccess() {
                runOnUiThread(() -> hintRegisterTextView.setText(R.string.bingbinhttp_onresponsenotsuccess));
            }

            @Override
            public void onJsonParseError() {
                runOnUiThread(() -> hintRegisterTextView.setText(R.string.bingbinhttp_onjsonparseerror));
            }

            @Override
            public void onNotValid(String errorStr) {
                runOnUiThread(() -> hintRegisterTextView.setText(errorStr));
            }

            @Override
            public void onTokenNotValid(String errorStr) {
                runOnUiThread(() -> hintRegisterTextView.setText(errorStr));
            }

            @Override
            public void onValid(JSONObject json) throws JSONException {
                String token = json.getString("token");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("status", "success");
                intent.putExtra("token", token);

                runOnUiThread(() -> {
                    showLoader(false);
                    setResult(RESULT_OK, intent);
                    RegisterActivity.this.finish();
                });
            }

            @Override
            public void onAnyError() {
                runOnUiThread(() -> showLoader(false));
            }
        };

        bbh.register(new BingBinCallback(action), email, firstname, pwd);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void showLoader(boolean show) {
        if (show) {
            registerProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            registerProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
