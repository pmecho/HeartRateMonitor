package com.smpete.heartrate.timer;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.smpete.heartrate.R;

public class TimerFragment extends Fragment implements TimePickerDialogFragment.TimePickerDialogHandler{
    private static final int REFERENCE_WARM_UP = 200;


    @InjectView(R.id.warm_up_time) Button mWarmUpTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hiit_setup, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    @OnClick(R.id.warm_up_time)
    public void onWarmUpTap() {
        HmsPickerBuilder builder = new HmsPickerBuilder()
                .setFragmentManager(getFragmentManager())
                .setTargetFragment(this)
                .setReference(REFERENCE_WARM_UP)
                .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        builder.show();
    }

    @Override
    public void onDialogTimeSet(int refernce, int minute, int second) {

    }
}
