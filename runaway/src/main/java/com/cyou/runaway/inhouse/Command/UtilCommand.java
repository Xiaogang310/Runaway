package com.cyou.runaway.inhouse.Command;

import android.util.Log;

import com.cyou.runaway.inhouse.Component.Util.AndroidUtil;
import com.cyou.runaway.inhouse.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Gang on 2016/9/26.
 */
public class UtilCommand extends CommandBase
{
    @Override
    public JSONObject execute(String args)
    {
        Log.d(toString(), "execute: " + args);
        AndroidUtil util = (AndroidUtil) SDKContainer.getInstance().getComponent(AndroidUtil.TAG);

        if (null == util)
        {
            Log.d(toString(), "execute: androidUtil is " + util);
            return null;
        }

        try
        {
            JSONObject json = new JSONObject(args);
            String func = json.getString(SDKContainer.COMMAND_FUNC);

            JSONObject result = new JSONObject();

            Method method = util.getClass().getMethod(func);

            try
            {
                String strRet = method.invoke(util).toString();
                result.put("type", "ok");
                result.put("result", strRet);
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
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

    @Override
    public String toString()
    {
        return "utilCmd";
    }
}
