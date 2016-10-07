package com.cyou.runaway.inhouse.Core.Marker;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.cyou.runaway.inhouse.Activity.MapActivity;
import com.cyou.runaway.inhouse.Control.MarkerView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiao on 2016/10/4.
 */

public class MapMarkerManager
{
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

    protected MarkerView mPlayerView;
    protected MarkerView mSchoolView;

    protected InfoWindow mInfoWindow;
    protected View mPlayerInfoView;
    protected View mSchoolInfoView;
    protected Point mOffset = new Point(10, 10);

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

    public void hideInfoWindow()
    {
        mMap.hideInfoWindow();
    }

    protected MapMarker createMainPlayerMarker(MarkerInfo info)
    {
        return  new MainPlayerMapMarker(this, info);
    }

    protected MapMarker createPlayerMarker(MarkerInfo info)
    {
        return new PlayerMapMarker(this, info);
    }

    protected MapMarker createSchoolMarker(MarkerInfo info)
    {
        return new SchoolMapMarker(this, info);
    }

    protected void init()
    {
        mOnMarkerClickListener = new MarkerClickListener();
        mPlayerMarkerMap = Collections.synchronizedMap(new HashMap<Marker, MapMarker>());
        mSchoolMarkerMap = Collections.synchronizedMap(new HashMap<Marker, MapMarker>());
        mMainPlayerMarker = null;

        mDefaultMainPlayerDescriptor = BitmapDescriptorFactory.fromAsset(mDefaultMainPlayerDescriptorPath);

        int marker_player_id = mMapActivity.getResources().getIdentifier("marker_player", "layout", mMapActivity.getPackageName());
        mPlayerView = new MarkerView(mMapActivity.inflate(marker_player_id));

        int marker_school_id = mMapActivity.getResources().getIdentifier("marker_school", "layout", mMapActivity.getPackageName());
        mSchoolView = new MarkerView(mMapActivity.inflate(marker_school_id));

        int dialog_player_id = mMapActivity.getResources().getIdentifier("dialog_player", "layout", mMapActivity.getPackageName());
        mPlayerInfoView = mMapActivity.inflate(dialog_player_id);

        int dialog_school_id = mMapActivity.getResources().getIdentifier("dialog_school", "layout", mMapActivity.getPackageName());
        mSchoolInfoView = mMapActivity.inflate(dialog_school_id);
    }

    public BitmapDescriptor getBitmapDescriptor(MarkerInfo info)
    {
        switch (info.markType)
        {
            case MT_MainPlayer:
                return mDefaultMainPlayerDescriptor;
            case MT_Player:
            {
                mPlayerView.setText("Player_Text", "我是玩家" + info.id);
                return mPlayerView.getBitmapDescriptor();
            }
            case MT_School:
            {
                mSchoolView.setText("School_Text", "我是帮会" + info.id);
                return mSchoolView.getBitmapDescriptor();
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

    protected Point getPopupPoint(Point center, Point src, Point windowSize, Point offset)
    {
        boolean bX = (src.x - center.x) > 0;
        boolean bY = (src.y - center.y) > 0;

        Point tmp = new Point();
        int px = bX ? -1 : 1;
        int py = bY ? -1 : 1;

        //原来的初始坐标是View的中下点
        tmp.x = src.x + px * (offset.x + windowSize.x /2);
        tmp.y = src.y + windowSize.y / 2 + py * (offset.y + windowSize.y / 2);

        return tmp;
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

                LatLng location = mapMarker.getLocation();
                Point point = mMap.getProjection().toScreenLocation(location);
                Point screenSize = new Point();
                getScreenSize(screenSize);
                Point center = new Point(screenSize.x / 2, screenSize.y / 2);
                Point viewSize = new Point(mPlayerInfoView.getWidth(), mPlayerInfoView.getHeight());
                Log.d("  ", "onMarkerClick: " + viewSize.toString());
                Point popupPoint = getPopupPoint(center, point, viewSize, mOffset);
                LatLng popupLocation = mMap.getProjection().fromScreenLocation(popupPoint);

                //TODO : FIX MEM LEAK?
                mInfoWindow = new InfoWindow(mPlayerInfoView, popupLocation, -47);
                mMap.showInfoWindow(mInfoWindow);

                return true;
            }
            else if (mSchoolTitle.equals(title))
            {
                MapMarker mapMarker = mSchoolMarkerMap.get(marker);

                LatLng location = mapMarker.getLocation();
                Point point = mMap.getProjection().toScreenLocation(location);
                Point screenSize = new Point();
                getScreenSize(screenSize);
                Point center = new Point(screenSize.x / 2, screenSize.y / 2);
                Point viewSize = new Point(mSchoolInfoView.getWidth(), mSchoolInfoView.getHeight());
                Point popupPoint = getPopupPoint(center, point, viewSize, mOffset);
                LatLng popupLocation = mMap.getProjection().fromScreenLocation(popupPoint);

                //TODO : FIX MEM LEAK?
                mInfoWindow = new InfoWindow(mSchoolInfoView, popupLocation, -47);
                mMap.showInfoWindow(mInfoWindow);

                return true;
            }

            return false;
        }
    }
}

