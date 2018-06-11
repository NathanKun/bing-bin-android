package io.bingbin.bingbinandroid;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

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

    public static RefWatcher getRefWatcher(Context context) {
        BingBinApp application = (BingBinApp) context.getApplicationContext();
        return application.refWatcher;
    }
}