package io.bingbin.bingbinandroid.views.loginActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private final int CANCEL = 2333;
    private final int SUCCESS = 23333;

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

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_register_button)
    void buttonOnClick(View view){
        String email = emailRegisterEdittext.getText().toString();
        String firstname = nameRegisterEdittext.getText().toString();
        String pwd = passwordRegistgerEdittext.getText().toString();
        String pwd2 = password2RegistgerEdittext.getText().toString();
        if(StringUtils.isAnyBlank(email, firstname, pwd, pwd2)) {
            hintRegisterTextView.setText("Veuillez renseigner tous les champs");
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            hintRegisterTextView.setText("L'adresse mail est incorrect");
            return;
        }
        if(!pwd.equals(pwd2)) {
            hintRegisterTextView.setText("Les mots de passe sont différents");
            return;
        }
        if(pwd.length() < 6) {
            hintRegisterTextView.setText("Le mots de passe est trop court");
            return;
        }
        registerProgressBar.setVisibility(View.VISIBLE);

        // BingBinHttp register
        // callback success => end activity and send user, loginProgressBar GONE
        // callback error => setText, loginProgressBar GONE
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    hintRegisterTextView.setText("Erreur de connexion");
                    registerProgressBar.setVisibility(View.GONE);});
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String hint = "";
                if (!response.isSuccessful()){
                    runOnUiThread(() -> hintRegisterTextView.setText("Request not success"));
                } else {
                    String body = response.body().string();
                    Log.d("bbh register", body);

                    String token = "";

                    try {
                        JSONObject res = new JSONObject(body);
                        if(res.getBoolean("valid")) {
                            token = res.getString("token");
                        } else {
                            hint = res.getString("error");
                        }
                    } catch (JSONException e) {
                        hint = "Response JSON error";
                        e.printStackTrace();
                    }

                    if(StringUtils.isNotBlank(token)) {
                        String finalToken = token;
                        runOnUiThread(() -> {
                            runOnUiThread(() -> registerProgressBar.setVisibility(View.GONE));
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("status", "success");
                            intent.putExtra("token", finalToken);
                            setResult(SUCCESS, intent);
                            RegisterActivity.this.finish();
                        });
                    } else {
                        String finalHint = hint;
                        runOnUiThread(() -> {
                            hintRegisterTextView.setText(finalHint);
                            registerProgressBar.setVisibility(View.GONE);
                        });
                    }
                }
            }
        };

        BingBinHttp bbh = new BingBinHttp();
        bbh.register(callback, email, firstname, pwd);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        setResult(CANCEL, intent);
        finish();
    }
}
