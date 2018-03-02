package io.bingbin.bingbinandroid.views.avatarActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class AvatarActivity extends AppCompatActivity {


    @Inject
    BingBinHttp bbh;

    private SmartUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        currentUser = UserSessionManager.getCurrentUser(this);
    }
}
