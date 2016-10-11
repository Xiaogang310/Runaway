package com.cyou.runaway.inhouse.Command;

import com.cyou.runaway.Processor.annotation.CommandAnnotation;
import com.cyou.runaway.Processor.annotation.FieldAnnotation;
import com.cyou.runaway.inhouse.Component.Activity.ActivityController;
import com.cyou.runaway.inhouse.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Gang on 2016/9/30.
 */
@CommandAnnotation(command = ActivityCommand.TAG)
public class ActivityCommand extends CommandBase
{
    public static final String TAG = "activityCmd";

    @FieldAnnotation()
    ActivityController mActivityController = null;

    @Override
    public JSONObject execute(String args)
    {
        if (null == mActivityController)
            mActivityController = (ActivityController) SDKContainer.getInstance().getComponent(ActivityController.TAG);

        try
        {
            JSONObject json = new JSONObject(args);
            String func = json.getString(SDKContainer.COMMAND_FUNC);

            JSONObject result = new JSONObject();
            Method method = mActivityController.getClass().getMethod(func);

            try
            {
                method.invoke(mActivityController).toString();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
                return null;
            }

            return result;

        }
        catch (JSONException e)
        {
            return null;
        }
        catch (NoSuchMethodException e)
        {
            return null;
        }
    }
}
