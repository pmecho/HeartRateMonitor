package com.smpete.heartrate;

import android.app.Application;

import com.smpete.heartrate.data.DataModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import dagger.internal.Modules;

public class HeartRateApplication extends Application {
    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = ObjectGraph.create(new HeartRateModule(this), new DataModule());
        mObjectGraph.inject(this);
    }

    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new HeartRateModule(this),
                new DataModule()
        );
    }

}
