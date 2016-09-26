package com.cyou.runaway.Component.Util;

import android.os.Build;

import com.cyou.runaway.Component.ComponentInterface;

import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/26.
 */
public class AndroidUtil implements ComponentInterface
{
    static public String TAG = "AndroidUtil";

    public AndroidUtil()
    {

    }

    public String model()
    {
        return Build.MODEL;
    }
}
