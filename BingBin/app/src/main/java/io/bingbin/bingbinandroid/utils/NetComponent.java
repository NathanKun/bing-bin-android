package io.bingbin.bingbinandroid.utils;

import javax.inject.Singleton;

import dagger.Component;
import io.bingbin.bingbinandroid.views.classifyActivity.ClassifyActivity;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import io.bingbin.bingbinandroid.views.loginActivity.RegisterActivity;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
import studios.codelight.smartloginlibrary.util.UserUtil;

/**
 * Dagger component
 * Created by NathanKun on 2018/2/13.
 */

@Singleton
@Component(modules={BbhModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(ClassifyActivity activity);
}
