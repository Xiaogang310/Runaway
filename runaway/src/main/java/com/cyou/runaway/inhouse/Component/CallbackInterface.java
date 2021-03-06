package com.cyou.runaway.inhouse.Component;

import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/26.
 */
abstract public class CallbackInterface
{
    protected String mRoot;
    protected String mCallback;

    abstract public void executeCallback(JSONObject json);

    public void setCallback(String root, String callback)
    {
        mRoot = root;
        mCallback = callback;
    }
}
