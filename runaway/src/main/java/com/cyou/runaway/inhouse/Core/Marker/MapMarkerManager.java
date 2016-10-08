package com.cyou.runaway.inhouse.Core.Marker;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.cyou.runaway.inhouse.Activity.MapActivity;
import com.cyou.runaway.inhouse.Control.MarkerView;
import com.cyou.runaway.inhouse.Core.MapConfig;
import com.cyou.runaway.inhouse.Core.Util.Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiao on 2016/10/4.
 */

public class MapMarkerManager
{
    String TAG = "MapMarkerManager";
    public int depthPlayer = 9;
    public int depthSchool = 10;
    public int depthMainPlayer = 11;

    protected String mMainPlayerTitle = "main";
    protected String mPlayerTitle = "player";
    protected String mSchoolTitle = "school";

    protected String mDefaultMainPlayerDescriptorPath = "main.png";

    protected BitmapDescriptor mDefaultMainPlayerDescriptor;

    protected Map<Marker, MapMarker> mPlayerMarkerMap;
    protected Map<Marker, MapMarker> mSchoolMarkerMap;
    protected MapMarker mMainPlayerMarker;

    protected BaiduMap mMap;
    protected MapActivity mMapActivity;
    protected MarkerClickListener mOnMarkerClickListener;

    protected Map<String, MarkerView> mMarkerViewMap;

    protected InfoWindow mInfoWindow;

    public MapMarkerManager(MapActivity mapActivity, BaiduMap map)
    {
        mMapActivity = mapActivity;
        mMap = map;

        init();

        mMap.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    public MapMarker createMarker(MarkerInfo info)
    {
        switch (info.markType)
        {
            case MT_MainPlayer:
                return createMainPlayerMarker(info);
            case MT_Player:
                return createPlayerMarker(info);
            case MT_School:
                return createSchoolMarker(info);
            default:break;
        }

        return null;
    }

    public void addMarkerView(String name, MarkerView view)
    {
        if (!mMarkerViewMap.containsKey(name))
            mMarkerViewMap.put(name, view);
    }

    public void hideInfoWindow()
    {
        mMap.hideInfoWindow();
    }

    protected MapMarker createMainPlayerMarker(MarkerInfo info)
    {
        return  new MapMarkerMainPlayer(this, info);
    }

    protected MapMarker createPlayerMarker(MarkerInfo info)
    {
        return new MapMarkerPlayer(this, info);
    }

    protected MapMarker createSchoolMarker(MarkerInfo info)
    {
        return new MapMarkerSchool(this, info);
    }

    protected void init()
    {
        mOnMarkerClickListener = new MarkerClickListener();
        mPlayerMarkerMap = Collections.synchronizedMap(new HashMap<Marker, MapMarker>());
        mSchoolMarkerMap = Collections.synchronizedMap(new HashMap<Marker, MapMarker>());
        mMarkerViewMap = Collections.synchronizedMap(new HashMap<String, MarkerView>());

        mMainPlayerMarker = null;

        mDefaultMainPlayerDescriptor = BitmapDescriptorFactory.fromAsset(mDefaultMainPlayerDescriptorPath);
    }

    public BitmapDescriptor getBitmapDescriptor(MarkerInfo info, Point size)
    {
        switch (info.markType)
        {
            case MT_MainPlayer:
                return mDefaultMainPlayerDescriptor;
            case MT_Player:
            {
                MarkerView view = mMarkerViewMap.get(MapConfig.LAYOUT_MARKER_PLAYER);
                view.setText("Player_Text", "我是玩家" + info.id);
                size.set(view.getViewSize().x, view.getViewSize().y);
                return view.getBitmapDescriptor();
            }
            case MT_School:
            {
                MarkerView view = mMarkerViewMap.get(MapConfig.LAYOUT_MARKER_SCHOOL);
                view.setText("School_Text", "我是帮会" + info.id);
                size.set(view.getViewSize().x, view.getViewSize().y);
                return view.getBitmapDescriptor();
            }

            default:break;
        }

        return null;
    }

    public Marker addMarker(MapMarker mapMarker)
    {
        Marker marker = (Marker)(mMap.addOverlay(mapMarker.getOptions()));
        MarkerInfo info = mapMarker.getInfo();

        if (null != marker)
        {
            switch (info.markType)
            {
                case MT_MainPlayer:
                    marker.setTitle(mMainPlayerTitle);
                    mMainPlayerMarker = mapMarker;
                    break;
                case MT_Player:
                    marker.setTitle(mPlayerTitle);
                    mPlayerMarkerMap.put(marker, mapMarker);
                    break;
                case MT_School:
                    marker.setTitle(mSchoolTitle);
                    mSchoolMarkerMap.put(marker, mapMarker);
                    break;
            }

            return marker;
        }
        else
        {
            return null;
        }
    }

    protected void getScreenSize(Point size)
    {
        WindowManager wm = (WindowManager)mMapActivity.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= 13)
            wm.getDefaultDisplay().getSize(size);
        else
        {
            size.set(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight());
        }
    }

    protected LatLng getPopupLocation(MapMarker srcMarker, MarkerView popupView)
    {
        LatLng srcLocation = srcMarker.getLocation();
        Point srcPoint = mMap.getProjection().toScreenLocation(srcLocation);
        Point screenSize = new Point();
        getScreenSize(screenSize);
        Point center = new Point(screenSize.x /2 , screenSize.y / 2);
        Point popupSize = popupView.getViewSize();
        Point markerSize = srcMarker.getSize();

        Point point = Helper.CalculatePopupPosition(center, srcPoint, markerSize, popupSize);
        return  mMap.getProjection().fromScreenLocation(point);
    }

    class MarkerClickListener implements BaiduMap.OnMarkerClickListener
    {
        @Override
        public boolean onMarkerClick(Marker marker)
        {
            String title = marker.getTitle();

            if (mMainPlayerTitle.equals(title))
            {
                Log.d("MarkerClickListener", "onMarkerClick: " + mMainPlayerTitle);
                hideInfoWindow();
                return true;
            }
            else if (mPlayerTitle.equals(title))
            {
                MapMarker mapMarker = mPlayerMarkerMap.get(marker);
                MarkerView view = mMarkerViewMap.get(MapConfig.LAYOUT_PLAYER_DIALOG);
                LatLng popupLocation = getPopupLocation(mapMarker, view);

                mInfoWindow = new InfoWindow(view.getView(), popupLocation, -47);
                mMap.showInfoWindow(mInfoWindow);

                return true;
            }
            else if (mSchoolTitle.equals(title))
            {
                MapMarker mapMarker = mSchoolMarkerMap.get(marker);

                MarkerView view = mMarkerViewMap.get(MapConfig.LAYOUT_SCHOOL_DIALOG);
                LatLng popupLocation = getPopupLocation(mapMarker, view);

                mInfoWindow = new InfoWindow(view.getView(), popupLocation, -47);
                mMap.showInfoWindow(mInfoWindow);

                return true;
            }

            return false;
        }
    }
}

