package com.smpete.heartrate.timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.smpete.heartrate.R;

public class TimerContainerView extends FrameLayout {

    public TimerContainerView(Context context) {
        super(context);
    }

    public TimerContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimerContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void showSetupView() {
        SetTimerView setTimerView = new SetTimerView(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        setTimerView.setLayoutParams(params);
        addView(setTimerView);
    }
}
