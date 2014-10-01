package com.smpete.heartrate;

import android.app.Application;

import com.smpete.heartrate.data.DataModule;
import com.smpete.heartrate.hr.HeartRateFragment;
import com.smpete.heartrate.timer.SetTimerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = DataModule.class,
        injects = {
                HeartRateApplication.class,
                HeartRateFragment.class,
                SetTimerFragment.class,
        }
)
public class HeartRateModule {
    private final HeartRateApplication mApp;

    public HeartRateModule(HeartRateApplication app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }
}
