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
    protected Point mSize = null;

    protected int mWidthMeasureSpec;
    protected int mHeightMeasureSpec;

    public MarkerView(View view)
    {
        mView = view;
        mViewMap = Collections.synchronizedMap(new HashMap<String, View>());

        mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    public View getView()
    {
        return mView;
    }

    public Point getViewSize()
    {
        if (null == mSize)
        {
            mView.measure(mWidthMeasureSpec, mHeightMeasureSpec);
            mSize = new Point(mView.getMeasuredWidth(), mView.getMeasuredHeight());
        }

        return mSize;
    }

    public BitmapDescriptor getBitmapDescriptor()
    {
        return BitmapDescriptorFactory.fromView(mView);
    }

    public void setText(String viewName, String text)
    {
        TextView textView = (TextView)getViewControl(viewName);

        if (null != textView)
            textView.setText(text);
    }

    public void setBitmap(String imageViewName, Bitmap bitmap)
    {
        ImageView imageView = (ImageView) getViewControl(imageViewName);

        if (null != imageView)
            imageView.setImageBitmap(bitmap);
    }

    protected View getViewControl(String name)
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
