package com.cyou.runaway;

import com.cyou.runaway.Command.CommandBase;
import com.cyou.runaway.Command.GPSCommand;
import com.cyou.runaway.Component.ComponentInterface;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gang on 2016/9/22.
 */
public class SDKContainer
{
    protected static SDKContainer msInstance = null;

    protected Map<String, CommandBase> mCommandMap;

    protected Map<String, ComponentInterface> mComponentMap;

    public String commandFuncName = "CommandFunc";

    private SDKContainer()
    {
        mCommandMap = new HashMap<>();
        mComponentMap = new HashMap<>();
    }

    public static SDKContainer getInstance()
    {
        if (null == msInstance)
        {
            msInstance = new SDKContainer();
        }

        return msInstance;
    }

    public static void jniCall(String cmd, String jsonParam)
    {
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

    public static void UnityCallback(String func, String jsonParam)
    {
        try
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("func", func);
            jsonObj.put("json", jsonParam);

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
