package com.smpete.heartrate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.smpete.heartrate.hr.HeartRateFragment;
import com.smpete.heartrate.timer.SetTimerFragment;
import com.smpete.heartrate.timer.TimerContainerView;
import com.smpete.heartrate.widget.SlidingTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class MainActivity extends ActionBarActivity {

    private static final String WORKER_FRAGMENT_KEY = "workerFragment";

    @Optional
    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Optional
    @InjectView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @Optional
    @InjectView(R.id.timer_container)
    TimerContainerView mTimerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        // TODO handle more eloquently
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Only in landscape
        if (mViewPager != null) {
            // Set up pager
            mViewPager.setAdapter(new ViewPagerAdapter(this, getSupportFragmentManager()));

            // Set up tabs
            mTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
            mTabLayout.setDistributeEvenly(true);
            mTabLayout.setViewPager(mViewPager);
        }

        if (mTimerContainer != null) {
            mTimerContainer.showSetupView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public ViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HeartRateFragment();
            } else {
                return new SetTimerFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.tab_heart_rate);
                case 1:
                    return mContext.getString(R.string.tab_timer);
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
