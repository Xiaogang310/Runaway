package com.cyou.runaway.Component;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cyou.runaway.SDKContainer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by Gang on 2016/9/22.
 */
public class UnityActivity extends UnityPlayerActivity implements ComponentInterface
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        new SDKContainer(new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (SDKContainer.UNITY_CALL == msg.what)
                {
                    SDKContainer.handleMessage(msg);
                }
            }
        });

        SDKContainer.getInstance().initialize();

        initGPS();
    }

    protected void initGPS()
    {
        LocationManager locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        GPSProvider gpsProvider = new GPSProvider(locMgr);
        SDKContainer.getInstance().registerComponent(gpsProvider.toString(), gpsProvider);
    }
}
