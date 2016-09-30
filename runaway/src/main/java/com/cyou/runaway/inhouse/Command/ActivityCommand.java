package com.cyou.runaway.inhouse.Command;

import com.cyou.runaway.inhouse.Component.Activity.ActivityController;
import com.cyou.runaway.inhouse.SDKContainer;

import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/30.
 */
public class ActivityCommand extends CommandBase
{
    @Override
    public JSONObject execute(String args)
    {
        ActivityController service = (ActivityController) SDKContainer.getInstance().getComponent(ActivityController.TAG);
        service.openMap();

        return null;
    }

    @Override
    public String toString()
    {
        return "activityCmd";
    }
}
