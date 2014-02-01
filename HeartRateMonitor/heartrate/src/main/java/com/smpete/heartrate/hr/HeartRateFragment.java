package com.smpete.heartrate.hr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.smpete.heartrate.R;

public class HeartRateFragment extends Fragment {

    private static final String SCAN_FRAGMENT_TAG = "scan";
    private static final int REQUEST_CODE_SCAN = 1001;

    public static final String EXTRAS_DEVICE_NAME = "deviceName";
    public static final String EXTRAS_DEVICE_ADDRESS = "deviceAddress";


    @InjectView(R.id.heart_rate_value) TextView mHeartRateValue;
    @InjectView(R.id.scan_button) Button mScanButton;

    private boolean mHeartRateConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mHeartRateConnected) {
            mHeartRateValue.setVisibility(View.VISIBLE);
            mScanButton.setVisibility(View.GONE);
        } else {
            mHeartRateValue.setVisibility(View.GONE);
            mScanButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(EXTRAS_DEVICE_NAME);
                String address = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);

                Log.d("XXX", "Name: " + name);
                Log.d("XXX", "Address: " + address);

                mHeartRateConnected = true;
            } else {
                mHeartRateConnected = false;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.scan_button)
    public void scan() {
        MonitorScanFragment scanFragment = new MonitorScanFragment();
        scanFragment.setTargetFragment(this, REQUEST_CODE_SCAN);
        scanFragment.show(getFragmentManager(), SCAN_FRAGMENT_TAG);
    }

}
