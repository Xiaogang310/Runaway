package com.cyou.runaway.inhouse.Activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cyou.runaway.inhouse.SDKContainer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by Gang on 2016/9/22.
 */
public class UnityActivity extends UnityPlayerActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        new SDKContainer(this, new Handler()
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
    }
}
