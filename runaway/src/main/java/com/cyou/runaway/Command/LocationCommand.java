package com.cyou.runaway.Command;

import android.util.Log;

import com.cyou.runaway.Component.Location.LocationService;
import com.cyou.runaway.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xiao on 2016/9/25.
 */
public class LocationCommand extends CommandBase
{
    @Override
    public JSONObject execute(String args)
    {
        Log.d(this.toString(), "execute: " + args);
        
        String funcName = null, callback;
        LocationService service = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);

        if (null == service)
        {
            Log.d(this.toString(), "execute: null");
            return null;
        }

        try
        {
            JSONObject jsonObj = new JSONObject(args);
            funcName = jsonObj.getString(SDKContainer.COMMAND_FUNC);
            callback = jsonObj.getString(SDKContainer.CALLBACK_NAME);

            if (null != callback)
                service.setCallback(callback);
        }
        catch (JSONException e)
        {
            if (null == funcName)
            {
                e.printStackTrace();
                return null;
            }
        }

        if (funcName.equals("start"))
            service.start();
        else if (funcName.equals("stop"))
            service.stop();

        return null;
    }

    @Override
    public String toString()
    {
        return "locationCmd";
    }
}