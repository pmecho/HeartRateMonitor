package com.smpete.heartrate;

import android.content.Context;
import android.content.SharedPreferences;

public enum AppPrefs {
    INSTANCE;

    private static final String PREFS_NAME = "prefs";

    private static final String KEY_LAST_HEART_RATE_ADDRESS = "lastHeartRateAddress";

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

}
