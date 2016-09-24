package com.cyou.runaway;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cyou.runaway.Command.CommandBase;
import com.cyou.runaway.Command.GPSCommand;
import com.cyou.runaway.Component.ComponentInterface;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Gang on 2016/9/22.
 */
public class SDKContainer
{
    String TAG = "UNITY_SDKContainer";
    public static  final int UNITY_CALL = 10086;
    public static  final String COMMAND_FUNC = "CommandFunc";

    protected static final String COMMAND = "Command";
    protected static final String JSON = "Json";

    protected static SDKContainer msInstance = null;

    protected Map<String, CommandBase> mCommandMap;

    protected Map<String, ComponentInterface> mComponentMap;

    protected Handler mMainLooperHandler = null;



    public SDKContainer(Handler handler)
    {
        msInstance = this;
        mMainLooperHandler = handler;

        mCommandMap = Collections.synchronizedMap(new HashMap<String, CommandBase>());
        mComponentMap = Collections.synchronizedMap(new HashMap<String, ComponentInterface>());
    }

    public static SDKContainer getInstance()
    {
        return msInstance;
    }

    public static void jniCall(String cmd, String jsonParam)
    {
        Log.d(msInstance.TAG, "jniCall: " + cmd + " " + jsonParam);
        msInstance.dispatch(cmd, jsonParam);
    }

    public static void handleMessage(Message msg)
    {
        if (UNITY_CALL == msg.what)
        {
            Bundle bundle = msg.getData();
            String cmd = bundle.getString(COMMAND);
            String json = bundle.getString(JSON);
            msInstance.execute(cmd, json);
        }
    }

    public static void unityCallback(String func, JSONObject jsonParam)
    {
        try
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("func", func);
            jsonObj.put("json", jsonParam);

            Log.d(msInstance.TAG, "unityCallback: " + func + " " + jsonParam);
            UnityPlayer.UnitySendMessage("Root", "OnAndroidCallback", jsonObj.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void initialize()
    {

        initCommands();
    }

    protected void registerCommand(String name, CommandBase cmd)
    {
        if (!mCommandMap.containsKey(name))
        {
            Log.d(TAG, "registerCommand: " + name );
            mCommandMap.put(name, cmd);
        }
    }

    public CommandBase getCommand(String name)
    {
        if (mCommandMap.containsKey(name))
        {
            return mCommandMap.get(name);
        }
        else
        {
            return null;
        }
    }

    public void registerComponent(String name, ComponentInterface component)
    {
        if (!mComponentMap.containsKey(name))
        {
            mComponentMap.put(name, component);
        }
    }

    public ComponentInterface getComponent(String name)
    {
        if (mComponentMap.containsKey(name))
        {
            return mComponentMap.get(name);
        }
        else
        {
            return null;
        }
    }

    protected void dispatch(String cmd, String jsonParam)
    {
        Message msg = mMainLooperHandler.obtainMessage(UNITY_CALL);
        Bundle bundle = new Bundle();
        bundle.putString(COMMAND, cmd);
        bundle.putString(JSON, jsonParam);
        msg.setData(bundle);
        msg.sendToTarget();
    }

    protected void execute(String cmd, String jsonParam)
    {
        Log.d(TAG, "execute: " + cmd + " " + jsonParam);
        CommandBase command = msInstance.getCommand(cmd);

        if (null != command)
        {
            command.execute(jsonParam);
        }
        else
        {
            msInstance.commandCallback();
        }
    }

    protected void registerCmd(CommandBase cmd)
    {
        registerCommand(cmd.toString(), cmd);
    }

    protected void initCommands()
    {
        registerCmd(new GPSCommand());
    }

    protected void commandCallback()
    {

    }
}
