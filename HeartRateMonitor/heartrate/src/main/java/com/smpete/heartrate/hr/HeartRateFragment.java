package com.smpete.heartrate.hr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.*;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

import java.util.List;
import java.util.UUID;

public class HeartRateFragment extends Fragment {

    private static final String SCAN_FRAGMENT_TAG = "scan";
    private static final int REQUEST_CODE_SCAN = 1001;

    public static final String EXTRAS_DEVICE_NAME = "deviceName";
    public static final String EXTRAS_DEVICE_ADDRESS = "deviceAddress";


    @InjectView(R.id.heart_rate_value) TextView mHeartRateValue;
    @InjectView(R.id.scan_button) Button mScanButton;

    private boolean mHeartRateConnected;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateViews();

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
                String name = data.getStringExtra(EXTRAS_DEVICE_NAME);
                mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);

                Log.d("XXX", "Name: " + name);
                Log.d("XXX", "Address: " + mDeviceAddress);

                mHeartRateConnected = true;

                mBluetoothLeService.connect(mDeviceAddress);
            } else {
                mHeartRateConnected = false;
            }
            updateViews();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateViews() {
        if (mHeartRateConnected) {
            mHeartRateValue.setVisibility(View.VISIBLE);
            mScanButton.setVisibility(View.GONE);
        } else {
            mHeartRateValue.setVisibility(View.GONE);
            mScanButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.scan_button)
    public void scan() {
        MonitorScanFragment scanFragment = new MonitorScanFragment();
        scanFragment.setTargetFragment(this, REQUEST_CODE_SCAN);
        scanFragment.show(getFragmentManager(), SCAN_FRAGMENT_TAG);
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.
                    ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                List<BluetoothGattService> supportedGattServices = mBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService gattService : supportedGattServices) {
                    BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT));
                    if (characteristic != null) {
                        mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                    }
                }
                // Show all the supported services and characteristics on the
                // user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                mHeartRateValue.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("XXX", "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
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
