package com.smpete.heartrate.timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.smpete.heartrate.R;

import java.util.concurrent.TimeUnit;

public class TimerFragment extends Fragment implements HmsPickerDialogFragment.HmsPickerDialogHandler,
        NumberPickerDialogFragment.NumberPickerDialogHandler {

    private static final String BUNDLE_KEY_HIIT_VALUES = "hiitValues";

    @InjectView(R.id.warm_up_time) Button mWarmUpButton;
    @InjectView(R.id.intervals_value) Button mIntervalsButton;
    @InjectView(R.id.work_time) Button mWorkButton;
    @InjectView(R.id.rest_time) Button mRestButton;
    @InjectView(R.id.cool_down_time) Button mCoolDownButton;

    private HiitValues mHiitValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hiit_setup, container, false);
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
