package io.bingbin.bingbinandroid.utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for BingBinHttp
 *
 * @author Junyang HE
 * Created by Junyang HE on 2018/2/13.
 */

@SuppressWarnings("WeakerAccess")
@Module
public class BbhModule {

    @Provides
    @Singleton
    public BingBinHttp provideBingBinHttp() {
        return new BingBinHttp();
    }
}
