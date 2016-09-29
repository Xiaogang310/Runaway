package com.cyou.runaway.inhouse.Command;

import android.util.Log;

import com.cyou.runaway.inhouse.Component.Location.LocationService;
import com.cyou.runaway.inhouse.SDKContainer;

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
        
        String funcName = null, gameObj =null, callback = null;
        LocationService service = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);

        if (null == service)
        {
            Log.d(this.toString(), "execute: null");
            return null;
        }

        JSONObject jsonObj = null;

        try
        {
            jsonObj = new JSONObject(args);
            funcName = jsonObj.getString(SDKContainer.COMMAND_FUNC);
            gameObj = jsonObj.getString(SDKContainer.GAME_OBJECT);
        }
        catch (JSONException e)
        {
            Log.d(toString(), "execute: no funcName or gameObjName");
            return null;
        }

        if (jsonObj.has(SDKContainer.CALLBACK_NAME))
        {
            try
            {
                callback = jsonObj.getString(SDKContainer.CALLBACK_NAME);
            }
            catch (JSONException e)
            {
                Log.d(toString(), "execute: get callback exception");

                return null;
            }

            service.setCallback(gameObj, callback);
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