package com.cyou.runaway.Command;

import com.cyou.runaway.Component.GPSProvider;
import com.cyou.runaway.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/22.
 */
public class GPSCommand extends CommandBase
{
    public GPSCommand()
    {

    }

    @Override
    public <T> T execute(String args)
    {
        String funcName;
        GPSProvider provider = (GPSProvider) SDKContainer.getInstance().getComponent("gps");

        try
        {
            JSONObject jsonObj = new JSONObject(args);
            funcName = jsonObj.getString(SDKContainer.getInstance().commandFuncName);
        }
        catch (JSONException e)
        {
            //TODO : process the exception
            e.printStackTrace();
            return null;
        }

        if (funcName.equals("start"))
            provider.start();
        else if (funcName.equals("stop"))
            provider.stop();

        return null;
    }

    @Override
    public String toString()
    {
        return "gps";
    }
}