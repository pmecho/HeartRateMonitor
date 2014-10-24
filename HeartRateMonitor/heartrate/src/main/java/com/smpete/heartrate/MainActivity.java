package com.smpete.heartrate;

import android.app.ActionBar;
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
import com.smpete.heartrate.widget.SlidingTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static final String WORKER_FRAGMENT_KEY = "workerFragment";

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;

    private ViewPagerAdapter mPagerAdapter;

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

        // Set up pager
        mPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        mTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setViewPager(mViewPager);

//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });
//
//        actionBar.addTab(actionBar.newTab().setText(R.string.tab_heart_rate).setTabListener(this));
//        actionBar.addTab(actionBar.newTab().setText(R.string.tab_timer).setTabListener(this));
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
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
