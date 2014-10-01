package com.smpete.heartrate.data;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        complete = false
)
public class DataModule {

    @Provides
    @Singleton
    AppPrefs provideAppPrefs(Application application) {
        return new AppPrefs(application);
    }

}
