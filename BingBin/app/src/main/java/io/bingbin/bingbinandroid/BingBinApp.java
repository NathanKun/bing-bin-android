package io.bingbin.bingbinandroid;

import android.app.Application;
import android.content.Context;

/**
 * BingBinApp Application class
 *
 * @author Junyang HE
 */

public class BingBinApp extends Application {

    private Context context;

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public Context getAppContext() {
        return this.context;
    }
}