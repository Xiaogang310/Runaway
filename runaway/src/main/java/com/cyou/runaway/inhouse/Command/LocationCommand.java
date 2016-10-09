package com.cyou.runaway.inhouse.Command;

import android.util.Log;

import com.cyou.runaway.inhouse.Component.Location.LocationService;
import com.cyou.runaway.inhouse.Core.Annotation.Doc.CommandAnnotation;
import com.cyou.runaway.inhouse.Core.Annotation.Doc.FieldAnnotation;
import com.cyou.runaway.inhouse.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xiao on 2016/9/25.
 */
@CommandAnnotation
public class LocationCommand extends CommandBase
{
    @FieldAnnotation
    LocationService mLocationService = null;

    @Override
    public JSONObject execute(String args)
    {
        Log.d(this.toString(), "execute: " + args);
        
        String funcName = null, root =null, callback = null;
        if (null == mLocationService)
            mLocationService = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);

        if (null == mLocationService)
        {
            Log.d(this.toString(), "execute: null");
            return null;
        }

        JSONObject jsonObj = null;

        try
        {
            jsonObj = new JSONObject(args);
            funcName = jsonObj.getString(SDKContainer.COMMAND_FUNC);
            root = jsonObj.getString(SDKContainer.ROOT);
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

            mLocationService.setCallback(root, callback);
        }

        if (funcName.equals("start"))
            mLocationService.start();
        else if (funcName.equals("stop"))
            mLocationService.stop();

        return null;
    }

    @Override
    public String commandName()
    {
        return "locationCmd";
    }
}