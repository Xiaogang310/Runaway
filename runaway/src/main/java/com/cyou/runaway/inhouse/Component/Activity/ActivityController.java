package com.cyou.runaway.inhouse.Component.Activity;

import android.app.Activity;
import android.content.Intent;

import com.cyou.runaway.inhouse.Activity.MapActivity;
import com.cyou.runaway.inhouse.Component.ComponentInterface;
import com.cyou.runaway.inhouse.Core.Annotation.Doc.MethodAnnotation;

/**
 * Created by Gang on 2016/9/30.
 */
public class ActivityController implements ComponentInterface
{
    public static String TAG = "ActivityController";

    protected Activity mMainActivity;

    public ActivityController(Activity mainActivity)
    {
        mMainActivity = mainActivity;
    }

    @MethodAnnotation
    public void openMap()
    {
        Intent intent = new Intent(mMainActivity, MapActivity.class);
        mMainActivity.startActivity(intent);
    }
}
