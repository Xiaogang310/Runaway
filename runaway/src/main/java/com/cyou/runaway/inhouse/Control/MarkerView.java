package com.cyou.runaway.inhouse.Control;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiao on 2016/10/7.
 */

public class MarkerView
{
    protected View mView;
    protected Map<String, View> mViewMap;
    protected Point mSize;

    public MarkerView(View view)
    {
        mView = view;
        mSize = new Point(mView.getWidth(), mView.getHeight());

        mViewMap = Collections.synchronizedMap(new HashMap<String, View>());
    }

    public View getAndroidView()
    {
        return mView;
    }

    public BitmapDescriptor getBitmapDescriptor()
    {
        return BitmapDescriptorFactory.fromView(mView);
    }

    public void setText(String viewName, String text)
    {
        TextView textView = (TextView)getView(viewName);

        if (null != textView)
            textView.setText(text);
    }

    public void setBitmap(String imageViewName, Bitmap bitmap)
    {
        ImageView imageView = (ImageView) getView(imageViewName);

        if (null != imageView)
            imageView.setImageBitmap(bitmap);
    }

    public Point getViewSize()
    {
        return mSize;
    }

    protected View getView(String name)
    {
        if (mViewMap.containsKey(name))
            return mViewMap.get(name);
        else
        {
            View targetView = mView.findViewWithTag(name);
            if (null != targetView)
                mViewMap.put(name, targetView);

            return targetView;
        }
    }
}
