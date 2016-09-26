package com.cyou.runaway.Command;

import android.util.Log;

import com.cyou.runaway.Component.Location.GPSProvider;
import com.cyou.runaway.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/22.
 */
@Deprecated //use LocationCommand instead

public class GPSCommand extends CommandBase
{
    public String TAG = "GPSCommand";
    public GPSCommand()
    {

    }

    @Override
    public JSONObject execute(String args)
    {
        String funcName;
        GPSProvider provider = (GPSProvider) SDKContainer.getInstance().getComponent("gps");

        try
        {
            JSONObject jsonObj = new JSONObject(args);
            funcName = jsonObj.getString(SDKContainer.COMMAND_FUNC);
        }
        catch (JSONException e)
        {
            //TODO : process the exception
            e.printStackTrace();
            return null;
        }

        Log.d("", "execute: " + funcName);
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
