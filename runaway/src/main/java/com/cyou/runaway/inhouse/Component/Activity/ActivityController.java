package com.cyou.runaway.inhouse.Component.Activity;

import android.app.Activity;
import android.content.Intent;

import com.cyou.runaway.Processor.annotation.MethodAnnotation;
import com.cyou.runaway.inhouse.Activity.MapActivity;
import com.cyou.runaway.inhouse.Component.ComponentInterface;

/**
 * Created by Gang on 2016/9/30.
 */
public class ActivityController implements ComponentInterface
{
    public static final String TAG = "ActivityController";

    protected Activity mMainActivity;

    public ActivityController(Activity mainActivity)
    {
        mMainActivity = mainActivity;
    }


    @MethodAnnotation(description = "open the map", type = MethodAnnotation.Method_Type.Post)
    public void openMap()
    {
        Intent intent = new Intent(mMainActivity, MapActivity.class);
        mMainActivity.startActivity(intent);
    }
}
