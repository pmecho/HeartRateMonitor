package com.smpete.heartrate;

import android.app.Application;

public class HeartRateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppPrefs.INSTANCE.init(this);
    }
}
