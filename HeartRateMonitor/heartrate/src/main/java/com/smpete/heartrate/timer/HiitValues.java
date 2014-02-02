package com.smpete.heartrate.timer;

import android.os.Parcel;
import android.os.Parcelable;
import com.smpete.heartrate.AppPrefs;

public class HiitValues implements Parcelable {
    private int mWarmUpSeconds;
    private int mIntervals;
    private int mWorkSeconds;
    private int mRestSeconds;
    private int mCoolDownSeconds;

    public HiitValues() {
        AppPrefs prefs = AppPrefs.INSTANCE;
        mWarmUpSeconds = prefs.getHiitWarmUp();
        mIntervals = prefs.getHiitIntervals();
        mWorkSeconds = prefs.getHiitWork();
        mRestSeconds = prefs.getHiitRest();
        mCoolDownSeconds = prefs.getHiitCoolDown();
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
        this.mWarmUpSeconds = warmUpSeconds;
    }

    public int getIntervals() {
        return mIntervals;
    }

    public void setIntervals(int intervals) {
        this.mIntervals = intervals;
    }

    public int getWorkSeconds() {
        return mWorkSeconds;
    }

    public void setWorkSeconds(int workSeconds) {
        this.mWorkSeconds = workSeconds;
    }

    public int getRestSeconds() {
        return mRestSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.mRestSeconds = restSeconds;
    }

    public int getCoolDownSeconds() {
        return mCoolDownSeconds;
    }

    public void setCoolDownSeconds(int coolDownSeconds) {
        this.mCoolDownSeconds = coolDownSeconds;
    }

    protected HiitValues(Parcel in) {
        mWarmUpSeconds = in.readInt();
        mIntervals = in.readInt();
        mWorkSeconds = in.readInt();
        mRestSeconds = in.readInt();
        mCoolDownSeconds = in.readInt();
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
