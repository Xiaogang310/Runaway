package com.cyou.runaway.inhouse.Core.Marker;

import android.graphics.Point;
import android.util.Log;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Xiao on 2016/10/4.
 */

abstract public class MapMarker
{
    public MapMarker(MapMarkerManager mgr, MarkerInfo info)
    {
        mInfo = info;
        mManager = mgr;
        mBitmapUri = null;
        mSize = new Point();
    }

    public void queryBitmap()
    {
        if (null == mBitmapUri)
        {
            onBitmapLoaded(mManager.getBitmapDescriptor(mInfo, mSize));
            Log.d("MapMarker", "queryBitmap: size" + mSize.toString());
        }
    }

    public void onBitmapLoaded(BitmapDescriptor bitmap)
    {
        mBitmapDescriptor = bitmap;
        setupMarker();
    }

    public void setBitmapUri(String bitmapUri)
    {
        mBitmapUri = bitmapUri;
    }

    public MarkerOptions getOptions()
    {
        return mOptions;
    }

    public MarkerInfo getInfo()
    {
        return mInfo;
    }

    public LatLng getLocation()
    {
        return mLocation;
    }

    public void setLocation(LatLng location)
    {
        mLocation = location;
    }

    public Point getSize()
    {
        return mSize;
    }
    abstract protected void setupMarker();

    MarkerInfo mInfo;
    Point mSize;
    MapMarkerManager mManager;
    Marker mMarker;
    MarkerOptions mOptions;
    LatLng mLocation;
    BitmapDescriptor mBitmapDescriptor;
    String mBitmapUri;
}
