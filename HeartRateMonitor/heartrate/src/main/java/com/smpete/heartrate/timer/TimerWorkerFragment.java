package com.smpete.heartrate.timer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

public class TimerWorkerFragment extends Fragment {

    private TimerListener mListener;

    // Used for calculating the time from the start taking into account the pause times
    long mStartTime = 0;
    long mAccumulatedTime = 0;
    private Handler mHandler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (TimerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TimerListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start();
    }

    public void start() {
        mStartTime = SystemClock.elapsedRealtime();
        mHandler.post(mTimeUpdateThread);
    }

    Runnable mTimeUpdateThread = new Runnable() {
        @Override
        public void run() {
            long curTime = SystemClock.elapsedRealtime();
            long totalTime = mAccumulatedTime + (curTime - mStartTime);
            mListener.timeUpdated(totalTime);

            mHandler.postDelayed(mTimeUpdateThread, 10);
        }
    };

}
