package com.smpete.heartrate.hr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.smpete.heartrate.HeartRateApplication;
import com.smpete.heartrate.data.AppPrefs;
import com.smpete.heartrate.R;

import java.util.List;

import javax.inject.Inject;

public class HeartRateFragment extends Fragment {

    private static final String SCAN_FRAGMENT_TAG = "scan";
    private static final int REQUEST_CODE_SCAN = 1001;

    private static final int MOVING_AVERAGE_LENGTH = 5;

    private static final int MONITOR_STATE_DISCONNECTED = 0;
    private static final int MONITOR_STATE_PREVIOUS_CONNECTING = 1;
    private static final int MONITOR_STATE_SCANNING = 2;
    private static final int MONITOR_STATE_CONNECTING = 3;
    private static final int MONITOR_STATE_CONNECTED = 4;


    public static final String EXTRAS_DEVICE_ADDRESS = "deviceAddress";


    @InjectView(R.id.heart_rate_value) TextView mHeartRateValue;
    @InjectView(R.id.action_button) Button mActionButton;

    private int mMonitorState;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    private boolean mFirstAutoConnect = true;

    private int[] mPreviousRates = new int[MOVING_AVERAGE_LENGTH];
    private int mPreviousIndex = 0;

    @Inject
    AppPrefs mPrefs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((HeartRateApplication) getActivity().getApplication()).inject(this);

        mDeviceAddress = mPrefs.getLastHeartRateAddress();

        Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, Activity.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onDetach();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);
                setState(MONITOR_STATE_CONNECTING);
                mBluetoothLeService.connect(mDeviceAddress);
            } else {
                setState(MONITOR_STATE_DISCONNECTED);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.action_button)
    public void onActionTapped() {
        setState(MONITOR_STATE_PREVIOUS_CONNECTING);
    }

    public void updateRate(String rate) {
        mPreviousRates[mPreviousIndex++] = Integer.valueOf(rate);
        int total = 0;
        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++) {
            if (mPreviousRates[i] != 0) {
                total += mPreviousRates[i];
            }
        }
        total /= MOVING_AVERAGE_LENGTH;
        mPreviousIndex %= MOVING_AVERAGE_LENGTH;

        mHeartRateValue.setText(String.format("%d", total));
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                setState(MONITOR_STATE_DISCONNECTED);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Register for heart rate
                List<BluetoothGattService> supportedGattServices = mBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService gattService : supportedGattServices) {
                    BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(BluetoothLeService.UUID_HEART_RATE_MEASUREMENT);
                    if (characteristic != null) {
                        mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                        mPrefs.setLastHeartRateAddress(mDeviceAddress);
                        setState(MONITOR_STATE_CONNECTED);
                        break;
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                updateRate(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void setState(int state) {
        mMonitorState = state;
        switch (state) {
            case MONITOR_STATE_DISCONNECTED:
                mActionButton.setEnabled(true);
                mActionButton.setText(R.string.status_connect);
                mHeartRateValue.setText(R.string.no_heart_rate);
                break;
            case MONITOR_STATE_PREVIOUS_CONNECTING:
                if (TextUtils.isEmpty(mDeviceAddress)) {
                    setState(MONITOR_STATE_SCANNING);
                } else {
                    connect();
                }
                break;
            case MONITOR_STATE_SCANNING:
                mActionButton.setEnabled(false);
                mActionButton.setText(R.string.status_scanning);
                MonitorScanFragment scanFragment = new MonitorScanFragment();
                scanFragment.setTargetFragment(HeartRateFragment.this, REQUEST_CODE_SCAN);
                scanFragment.show(getFragmentManager(), SCAN_FRAGMENT_TAG);
                break;
            case MONITOR_STATE_CONNECTING:
                connect();
                break;
            case MONITOR_STATE_CONNECTED:
                mActionButton.setText(R.string.status_connected);
                // TODO Fade out
                break;
        }
    }

    private void connect() {
        mActionButton.setEnabled(false);
        mActionButton.setText(R.string.status_connecting);
        mBluetoothLeService.connect(mDeviceAddress);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((mFirstAutoConnect && mMonitorState == MONITOR_STATE_PREVIOUS_CONNECTING) || mMonitorState == MONITOR_STATE_CONNECTING) {
                    mBluetoothLeService.disconnect();
                    setState(MONITOR_STATE_DISCONNECTED);
                }
                else if (!mFirstAutoConnect && mMonitorState == MONITOR_STATE_PREVIOUS_CONNECTING) {
                    mBluetoothLeService.disconnect();
                    setState(MONITOR_STATE_SCANNING);
                }
                mFirstAutoConnect = false;
            }
        }, 5000);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("XXX", "Unable to initialize Bluetooth");
            }

            // Auto connect to last device
            if (mDeviceAddress != null) {
                setState(MONITOR_STATE_PREVIOUS_CONNECTING);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
