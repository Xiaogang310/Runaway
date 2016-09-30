package com.cyou.runaway.inhouse.Component.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import com.cyou.runaway.inhouse.Component.ComponentInterface;

/**
 * Created by Gang on 2016/9/26.
 */
public class AndroidUtil extends BroadcastReceiver implements ComponentInterface
{
    static public String TAG = "AndroidUtil";

    protected Activity mMainActivity = null;
    protected int mEnergyPercent = 0;

    public AndroidUtil(Activity activity)
    {
        mMainActivity = activity;

        if (null != mMainActivity)
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            mMainActivity.registerReceiver(this, filter);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int i = 0;
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);

        if (scale > 0)
            i = (100 * level) / scale;

        mEnergyPercent = i;
    }

    public String model()
    {
        return Build.MODEL;
    }
}