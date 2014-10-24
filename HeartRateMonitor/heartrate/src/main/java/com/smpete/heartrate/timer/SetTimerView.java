package com.smpete.heartrate.timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridLayout;

import com.smpete.heartrate.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetTimerView extends GridLayout {

    @InjectView(R.id.warm_up_time)
    Button mWarmUpButton;
    @InjectView(R.id.intervals_value)
    Button mIntervalsButton;
    @InjectView(R.id.work_time)
    Button mWorkButton;
    @InjectView(R.id.rest_time)
    Button mRestButton;
    @InjectView(R.id.cool_down_time)
    Button mCoolDownButton;

    // TODO Inject?
    SetTimerPresenter mPresenter;

    public SetTimerView(Context context) {
        super(context);
        init();
    }

    public SetTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SetTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SetTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.set_timer, this, true);
        ButterKnife.inject(this);
        setColumnCount(2);
        setRowCount(6);

        mPresenter = new SetTimerPresenter();

    }

    @SuppressWarnings("UnusedDeclaration")
    @OnClick(R.id.start_button)
    public void onStartButtonClicked() {
        mPresenter.onStartPressed();
    }

    @OnClick(R.id.intervals_value)
    public void onIntervalsTapped() {
//        NumberPickerBuilder builder = new NumberPickerBuilder()
//                .setFragmentManager(getFragmentManager())
//                .setTargetFragment(this)
//                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
//                .setMinNumber(1)
//                .setDecimalVisibility(View.GONE)
//                .setPlusMinusVisibility(View.GONE);
//        builder.show();
    }


}
