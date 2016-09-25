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
    public <T> T execute(String args)
    {
        Log.d(this.toString(), "execute: " + args);
        
        String funcName;
        LocationService service = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);

        if (null == service)
            Log.d(this.toString(), "execute: null");

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
