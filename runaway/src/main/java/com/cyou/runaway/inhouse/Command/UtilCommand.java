package com.cyou.runaway.inhouse.Command;

import android.util.Log;

import com.cyou.runaway.Processor.annotation.CommandAnnotation;
import com.cyou.runaway.Processor.annotation.FieldAnnotation;
import com.cyou.runaway.inhouse.Component.Util.AndroidUtil;
import com.cyou.runaway.inhouse.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Gang on 2016/9/26.
 */
@CommandAnnotation(command = UtilCommand.TAG)
public class UtilCommand extends CommandBase
{
    public static final String TAG = "utilCmd";

    @FieldAnnotation
    AndroidUtil mAndroidUtil = null;

    @Override
    public JSONObject execute(String args)
    {
        Log.d(toString(), "execute: " + args);
        if (null == mAndroidUtil)
            mAndroidUtil = (AndroidUtil) SDKContainer.getInstance().getComponent(AndroidUtil.TAG);

        if (null == mAndroidUtil)
        {
            Log.d(toString(), "execute: androidUtil is " + mAndroidUtil);
            return null;
        }

        try
        {
            JSONObject json = new JSONObject(args);
            String func = json.getString(SDKContainer.COMMAND_FUNC);

            JSONObject result = new JSONObject();

            Method method = mAndroidUtil.getClass().getMethod(func);

            try
            {
                String strRet = method.invoke(mAndroidUtil).toString();
                if (null != strRet)
                {
                    result.put("type", "ok");
                    result.put("result", strRet);
                }
                else
                {
                    result.put("type", "error");
                    result.put("result", "null");
                }
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