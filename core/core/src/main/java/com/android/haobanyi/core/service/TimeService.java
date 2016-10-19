package com.android.haobanyi.core.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;


public class TimeService extends Service {

    public static final String ACTION_TIME_TICK = "cn.septenary.ntptime.action.TIME_TICK";

    public static final int CMD_NTP = 0x01;

    private android.os.Handler mHandler;

    public TimeService() {
        mHandler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mTimeBroadcastReceiver, filter);
        mTicker.run();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int cmd = intent.getIntExtra("cmd", 0);
            dispatchCMD(cmd);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void dispatchCMD(int command) {
        switch (command) {
            case CMD_NTP:
                NTPTime.getInstance().updteNTPTime();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mTicker);
        unregisterReceiver(mTimeBroadcastReceiver);
    }

    private BroadcastReceiver mTimeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_TIME_TICK:
                    // Notify clock refresh
                    onTimeChanged();
                    break;
                case Intent.ACTION_TIME_CHANGED:
                case Intent.ACTION_TIMEZONE_CHANGED:
                    NTPTime.getInstance().updteNTPTime();
                    break;
            }
        }
    };

    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            mHandler.postAtTime(mTicker, next);
        }
    };

    private void onTimeChanged() {
        Intent intent = new Intent(ACTION_TIME_TICK);
        long time = NTPTime.getInstance().getCurrentTime();
        intent.putExtra("time", time);
        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
    }
}
