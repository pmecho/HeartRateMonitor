package com.smpete.heartrate.timer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.smpete.heartrate.R;

import java.util.concurrent.TimeUnit;

public class SetTimerFragment extends Fragment implements HmsPickerDialogFragment.HmsPickerDialogHandler,
        NumberPickerDialogFragment.NumberPickerDialogHandler, TimerListener {

    private static final String BUNDLE_KEY_HIIT_VALUES = "hiitValues";

    // Setup views
    @InjectView(R.id.warm_up_time) Button mWarmUpButton;
    @InjectView(R.id.intervals_value) Button mIntervalsButton;
    @InjectView(R.id.work_time) Button mWorkButton;
    @InjectView(R.id.rest_time) Button mRestButton;
    @InjectView(R.id.cool_down_time) Button mCoolDownButton;

    // Timer views
    @InjectView(R.id.interval_type) TextView mStateText;
    @InjectView(R.id.time_text) TextView mTimeText;
    @InjectView(R.id.rep_counter) TextView mRepCounter;

    // Container views
    @InjectView(R.id.setup_layout) View mSetupLayout;
    @InjectView(R.id.timer_layout) View mTimerLayout;

    private HiitValues mHiitValues;
    private int mHiitState;
    private int mCompletedReps;
    private TimerControlListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (TimerControlListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TimerControlListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.inject(this, v);

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_HIIT_VALUES)) {
            mHiitValues = savedInstanceState.getParcelable(BUNDLE_KEY_HIIT_VALUES);
        } else {
            mHiitValues = new HiitValues();
        }

        mWarmUpButton.setText(formatTime(mHiitValues.getWarmUpSeconds()));
        mIntervalsButton.setText(String.format("%d", mHiitValues.getIntervals()));
        mWorkButton.setText((formatTime(mHiitValues.getWorkSeconds())));
        mRestButton.setText((formatTime(mHiitValues.getRestSeconds())));
        mCoolDownButton.setText((formatTime(mHiitValues.getCoolDownSeconds())));

        mTimerLayout.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHiitValues.saveToPrefs();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_KEY_HIIT_VALUES, mHiitValues);
    }

    private void showTimer() {
        // Animate
        ObjectAnimator a = ObjectAnimator.ofFloat(mSetupLayout, View.SCALE_X, 1f, 0f);
        a.setInterpolator(new AccelerateInterpolator());
        a.setDuration(125);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSetupLayout.setVisibility(View.GONE);
                mTimerLayout.setScaleX(0);
                mTimerLayout.setVisibility(View.VISIBLE);
                ObjectAnimator b =
                        ObjectAnimator.ofFloat(mTimerLayout, View.SCALE_X, 0f, 1f);
                b.setInterpolator(new DecelerateInterpolator());
                b.setDuration(225);
                b.start();
            }
        });
        a.start();
    }

    private void showSetup() {
        // Animate
        ObjectAnimator a = ObjectAnimator.ofFloat(mTimerLayout, View.SCALE_X, 1f, 0f);
        a.setInterpolator(new AccelerateInterpolator());
        a.setDuration(125);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mTimerLayout.setVisibility(View.GONE);
                mSetupLayout.setScaleX(0);
                mSetupLayout.setVisibility(View.VISIBLE);
                ObjectAnimator b =
                        ObjectAnimator.ofFloat(mSetupLayout, View.SCALE_X, 0f, 1f);
                b.setInterpolator(new DecelerateInterpolator());
                b.setDuration(225);
                b.start();
            }
        });
        a.start();
    }

    /*
     * Setup methods
     */

    private void showTimePicker(int reference) {
        HmsPickerBuilder builder = new HmsPickerBuilder()
                .setFragmentManager(getFragmentManager())
                .setTargetFragment(this)
                .setReference(reference)
                .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        builder.show();
    }

    private String formatTime(int seconds) {
        return String.format("%d:%02d",
                TimeUnit.SECONDS.toMinutes(seconds),
                TimeUnit.SECONDS.toSeconds(seconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
    }

    /*
     * Timer methods
     */

    private void transitionToWarmUp() {
        setHiitState(HiitUtils.HIIT_STATE_WARM_UP);

        Spannable span = new SpannableString("4167");
        span.setSpan(new RelativeSizeSpan(0.8f), 2, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTimeText.setText(span);
    }

    private void transitionToWorkInterval() {
        setHiitState(HiitUtils.HIIT_STATE_WORK);
    }

    private void transitionToRestInterval() {
        setHiitState(HiitUtils.HIIT_STATE_REST);
        mCompletedReps++;
    }

    private void transitionToCoolDown() {
        setHiitState(HiitUtils.HIIT_STATE_COOL_DOWN);
        mCompletedReps++;
    }

    private void setHiitState(int newState) {
        mHiitState = newState;
        mStateText.setText(getResources().getStringArray(R.array.hiit_state_text)[newState]);
    }

    @Override
    public void timeUpdated(long millis) {
        long seconds = millis / 100;
        mTimeText.setText(String.valueOf(seconds));
    }

    @Override
    public void stateUpdate(int state) {

    }

    @Override
    public void repUpdate(int rep) {

    }

    /*
     * On Clicks
     */

    @OnClick({R.id.warm_up_time, R.id.work_time, R.id.rest_time, R.id.cool_down_time})
    public void onTimeTapped(View v) {
        showTimePicker(v.getId());
    }

    @OnClick(R.id.intervals_value)
    public void onIntervalsTapped() {
        NumberPickerBuilder builder = new NumberPickerBuilder()
                .setFragmentManager(getFragmentManager())
                .setTargetFragment(this)
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setMinNumber(1)
                .setDecimalVisibility(View.GONE)
                .setPlusMinusVisibility(View.GONE);
        builder.show();
    }

    @OnClick(R.id.start_button)
    public void onStartTapped() {
        showTimer();
        mListener.start();
    }

    /*
     * Time/Number dialog handlers
     */

    @Override
    public void onDialogHmsSet(int reference, int minutes, int seconds) {
        int totalSeconds = minutes * 60 + seconds;
        Button button = null;
        switch (reference) {
            case R.id.warm_up_time:
                mHiitValues.setWarmUpSeconds(totalSeconds);
                button = mWarmUpButton;
                break;
            case R.id.work_time:
                mHiitValues.setWorkSeconds(totalSeconds);
                button = mWorkButton;
                break;
            case R.id.rest_time:
                mHiitValues.setRestSeconds(totalSeconds);
                button = mRestButton;
                break;
            case R.id.cool_down_time:
                mHiitValues.setCoolDownSeconds(totalSeconds);
                button = mCoolDownButton;
                break;
        }

        if (button != null) {
            button.setText(formatTime(totalSeconds));
        }
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        mHiitValues.setIntervals(number);
        mIntervalsButton.setText(String.format("%d", number));
    }
}
