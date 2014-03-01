package com.smpete.heartrate.timer;

import android.os.Parcel;
import android.os.Parcelable;
import com.smpete.heartrate.AppPrefs;

public class HiitValues implements Parcelable {
    public static int HIIT_STATE_WARM_UP = 0;
    public static int HIIT_STATE_WORK = 1;
    public static int HIIT_STATE_REST = 2;
    public static int HIIT_STATE_COOL_DOWN = 3;

    private int mWarmUpSeconds;
    private int mIntervals;
    private int mWorkSeconds;
    private int mRestSeconds;
    private int mCoolDownSeconds;

    private int mCurrentState;
    private int mCurrentRep;
    private long mMillisRemaining;

    public HiitValues() {
        AppPrefs prefs = AppPrefs.INSTANCE;
        mWarmUpSeconds = prefs.getHiitWarmUp();
        mIntervals = prefs.getHiitIntervals();
        mWorkSeconds = prefs.getHiitWork();
        mRestSeconds = prefs.getHiitRest();
        mCoolDownSeconds = prefs.getHiitCoolDown();

        mCurrentState = HIIT_STATE_WARM_UP;
        mCurrentRep = 0;
        mMillisRemaining = mWarmUpSeconds * 1000;
    }

    protected HiitValues(Parcel in) {
        mWarmUpSeconds = in.readInt();
        mIntervals = in.readInt();
        mWorkSeconds = in.readInt();
        mRestSeconds = in.readInt();
        mCoolDownSeconds = in.readInt();

        mCurrentState = in.readInt();
        mCurrentRep = in.readInt();
        mMillisRemaining = in.readLong();
    }

    public void saveToPrefs() {
        AppPrefs prefs = AppPrefs.INSTANCE;
        prefs.setHiitWarmUp(mWarmUpSeconds);
        prefs.setHiitIntervals(mIntervals);
        prefs.setHiitWork(mWorkSeconds);
        prefs.setHiitRest(mRestSeconds);
        prefs.setHiitCoolDown(mCoolDownSeconds);
    }

    public int getWarmUpSeconds() {
        return mWarmUpSeconds;
    }

    public void setWarmUpSeconds(int warmUpSeconds) {
        mWarmUpSeconds = warmUpSeconds;
        mMillisRemaining = mWarmUpSeconds * 1000;

    }

    public int getIntervals() {
        return mIntervals;
    }

    public void setIntervals(int intervals) {
        mIntervals = intervals;
    }

    public int getWorkSeconds() {
        return mWorkSeconds;
    }

    public void setWorkSeconds(int workSeconds) {
        mWorkSeconds = workSeconds;
    }

    public int getRestSeconds() {
        return mRestSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        mRestSeconds = restSeconds;
    }

    public int getCoolDownSeconds() {
        return mCoolDownSeconds;
    }

    public void setCoolDownSeconds(int coolDownSeconds) {
        mCoolDownSeconds = coolDownSeconds;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }

    public int getCurrentRep() {
        return mCurrentRep;
    }

    public void setCurrentRep(int currentRep) {
        mCurrentRep = currentRep;
    }

    public long getMillisRemaining() {
        return mMillisRemaining;
    }

    public void setMillisRemaining(long millisRemaining) {
        mMillisRemaining = millisRemaining;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mWarmUpSeconds);
        dest.writeInt(mIntervals);
        dest.writeInt(mWorkSeconds);
        dest.writeInt(mRestSeconds);
        dest.writeInt(mCoolDownSeconds);

        dest.writeInt(mCurrentState);
        dest.writeInt(mCurrentRep);
        dest.writeLong(mMillisRemaining);
    }

    public static final Parcelable.Creator<HiitValues> CREATOR = new Parcelable.Creator<HiitValues>() {
        @Override
        public HiitValues createFromParcel(Parcel in) {
            return new HiitValues(in);
        }

        @Override
        public HiitValues[] newArray(int size) {
            return new HiitValues[size];
        }
    };
}
