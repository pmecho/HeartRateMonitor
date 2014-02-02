package com.smpete.heartrate;

import android.content.Context;
import android.content.SharedPreferences;

public enum AppPrefs {
    INSTANCE;

    private static final String PREFS_NAME = "prefs";

    private static final String KEY_LAST_HEART_RATE_ADDRESS = "lastHeartRateAddress";
    private static final String KEY_HIIT_WARM_UP = "hiitWarmUp";
    private static final String KEY_HIIT_INTERVALS = "hiitIntervals";
    private static final String KEY_HIIT_WORK = "hiitWork";
    private static final String KEY_HIIT_REST = "hiitRest";
    private static final String KEY_HIIT_COOL_DOWN = "hiitCoolDown";

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;

    public void init(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFS_NAME, 0);
        mEditor = mSharedPrefs.edit();
    }

    public void setLastHeartRateAddress(String address) {
        mEditor.putString(KEY_LAST_HEART_RATE_ADDRESS, address).apply();
    }

    public String getLastHeartRateAddress() {
        return mSharedPrefs.getString(KEY_LAST_HEART_RATE_ADDRESS, null);
    }

    public void setHiitWarmUp(int seconds) {
        mEditor.putInt(KEY_HIIT_WARM_UP, seconds).apply();
    }

    public int getHiitWarmUp() {
        return mSharedPrefs.getInt(KEY_HIIT_WARM_UP, 300);
    }

    public void setHiitIntervals(int intervals) {
        mEditor.putInt(KEY_HIIT_INTERVALS, intervals).apply();
    }

    public int getHiitIntervals() {
        return mSharedPrefs.getInt(KEY_HIIT_INTERVALS, 7);
    }

    public void setHiitWork(int seconds) {
        mEditor.putInt(KEY_HIIT_WORK, seconds).apply();
    }

    public int getHiitWork() {
        return mSharedPrefs.getInt(KEY_HIIT_WORK, 45);
    }

    public void setHiitRest(int seconds) {
        mEditor.putInt(KEY_HIIT_REST, seconds).apply();
    }

    public int getHiitRest() {
        return mSharedPrefs.getInt(KEY_HIIT_REST, 60);
    }

    public void setHiitCoolDown(int seconds) {
        mEditor.putInt(KEY_HIIT_COOL_DOWN, seconds).apply();
    }

    public int getHiitCoolDown() {
        return mSharedPrefs.getInt(KEY_HIIT_COOL_DOWN, 300);
    }

}
