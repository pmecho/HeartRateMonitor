package com.smpete.heartrate;

import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.smpete.heartrate.hr.HeartRateFragment;
import com.smpete.heartrate.timer.SetTimerFragment;
import com.smpete.heartrate.timer.TimerControlListener;
import com.smpete.heartrate.timer.TimerListener;
import com.smpete.heartrate.timer.TimerWorkerFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, TimerListener, TimerControlListener {

    private static final String WORKER_FRAGMENT_KEY = "workerFragment";

    @InjectView(R.id.view_pager) ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // TODO handle more eloquently
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up pager
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText(R.string.tab_heart_rate).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_timer).setTabListener(this));
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

    /*
     * Timer Listener
     */

    @Override
    public void timeUpdated(long millis) {
        mPagerAdapter.getCurrentTimerListener().timeUpdated(millis);
    }

    @Override
    public void stateUpdate(int state) {
        mPagerAdapter.getCurrentTimerListener().stateUpdate(state);
    }

    @Override
    public void repUpdate(int rep) {
        mPagerAdapter.getCurrentTimerListener().repUpdate(rep);
    }

    @Override
    public void start() {
        TimerWorkerFragment frag = (TimerWorkerFragment) getSupportFragmentManager().findFragmentByTag(WORKER_FRAGMENT_KEY);
        if (frag == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            frag = new TimerWorkerFragment();
            ft.add(frag, WORKER_FRAGMENT_KEY);
            ft.commit();
        } else {
            frag.start();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private TimerListener mCurrentFragment;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
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
        public int getCount() {
            return 2;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (mCurrentFragment != object) {
                mCurrentFragment = (TimerListener) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        public TimerListener getCurrentTimerListener() {
            return mCurrentFragment;
        }
    }

}
