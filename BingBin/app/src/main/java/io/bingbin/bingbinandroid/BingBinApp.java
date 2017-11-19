package io.bingbin.bingbinandroid;

import android.app.Application;
import android.content.Context;

/**
 * Created by Junyang HE on 18/11/2017.
 * For returning Application Context
 */

public class BingBinApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        BingBinApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return BingBinApp.context;
    }
}