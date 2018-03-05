package io.bingbin.bingbinandroid;

import android.app.Application;
import android.content.Context;

import io.bingbin.bingbinandroid.utils.BbhModule;
import io.bingbin.bingbinandroid.utils.DaggerNetComponent;
import io.bingbin.bingbinandroid.utils.NetComponent;

/**
 * BingBinApp Application class
 *
 * @author Junyang HE
 */

public class BingBinApp extends Application {

    private Context context;
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();

        netComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .bbhModule(new BbhModule())
                .build();

    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    @SuppressWarnings("unused")
    public Context getAppContext() {
        return this.context;
    }
}