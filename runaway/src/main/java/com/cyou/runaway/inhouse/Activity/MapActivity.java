package com.cyou.runaway.inhouse.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cyou.runaway.inhouse.Component.Location.LocationService;
import com.cyou.runaway.inhouse.Core.Marker.MapMarker;
import com.cyou.runaway.inhouse.Core.Marker.MapMarkerManager;
import com.cyou.runaway.inhouse.Core.Marker.MarkerInfo;
import com.cyou.runaway.inhouse.SDKContainer;


/**
 * Created by Gang on 2016/9/27.
 */
public class MapActivity extends Activity
{
    protected String TAG = "MapActivity";
    protected FrameLayout mLayout;
    protected MapView mMapView;
    protected BaiduMap mMap;
    protected BDLocationListener mLocationListener;
    protected MapClickListener mMapClickListener;
    protected LayoutInflater mLayoutInflater;
    protected MapViewLayoutParams.Builder mMapViewBuilder;
    protected MapMarkerManager mMarkerManager;

    protected LocationService mLocationService;

    protected double mLatOffset = 0.004;
    protected double mLngOffset = 0.004;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mLayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMapViewBuilder = new MapViewLayoutParams.Builder();
        mLocationListener = new MapLocationListener();
        mMapClickListener = new MapClickListener();

        mMapView = new MapView(this, new BaiduMapOptions());
        mMap = mMapView.getMap();
        mMap.setOnMapClickListener(mMapClickListener);
        mMap.setMyLocationEnabled(true);
        mMarkerManager = new MapMarkerManager(this, mMap);

        mLocationService = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);
        mLocationService.startWithListener(mLocationListener);

        initView(this);
        setContentView(mLayout);
    }

    public View inflate(int id)
    {
        return mLayoutInflater.inflate(id, null);
    }

    protected void initSubview(LatLng mainPlayerLocation)
    {
//        int id = getResources().getIdentifier("dialog_player", "layout", getPackageName());
//        View subview = mLayoutInflater.inflate(id, null);
//
//        //mMap.getProjection().toScreenLocation(mainPlayerLocation);
//
//        MapViewLayoutParams params = mMapViewBuilder.point(mMap.getProjection().toScreenLocation(mainPlayerLocation)).width(300).height(300).build();
//        mMapView.addView(subview, params);
    }

    protected void initOverlay(LatLng mainPlayerLocation)
    {
        if (null == mainPlayerLocation)
            return;

        //create main player
        MapMarker marker = mMarkerManager.createMarker(new MarkerInfo(MarkerInfo.MarkerType.MT_MainPlayer, 0));
        marker.setLocation(mainPlayerLocation);

        marker.queryBitmap();

        //create other players
        for (int i = 1; i < 10; ++i)
        {
            double randomLat = Math.random();
            double randomLng = Math.random();

            LatLng latLng = new LatLng(Math.pow(-1, i) * randomLat * mLatOffset + mainPlayerLocation.latitude,
                    Math.pow(-1, i) *randomLng * mLngOffset + mainPlayerLocation.longitude);

            marker = mMarkerManager.createMarker(new MarkerInfo(MarkerInfo.MarkerType.MT_Player, i));
            marker.setLocation(latLng);
            marker.queryBitmap();
        }

        //create school
        double randomLat = Math.random();
        double randomLng = Math.random();

        LatLng latLng = new LatLng(randomLat * mLatOffset + mainPlayerLocation.latitude,
                randomLng * mLngOffset + mainPlayerLocation.longitude);

        marker = mMarkerManager.createMarker(new MarkerInfo(MarkerInfo.MarkerType.MT_School, 0));
        marker.setLocation(latLng);
        marker.queryBitmap();
    }


    protected void initView(Context context)
    {
        mLayout = new FrameLayout(this);
        mLayout.addView(mMapView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mMapView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mLocationService.startWithListener(mLocationListener);
        mMapView.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mLocationService.stopWithListener(mLocationListener);
        mMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    class MapLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {

            if (null == bdLocation || null == mMapView)
                return;

            Log.d(TAG, "onReceiveLocation: map location");

            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(1000)
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            mMap.setMyLocationData(locationData);

            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            MapStatus status = builder.target(ll).zoom(17.0f).overlook(-30.0f).build();
            mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
            mLocationService.stopWithListener(this);

            initOverlay(ll);
        }
    }

    class MapClickListener implements BaiduMap.OnMapClickListener
    {
        @Override
        public void onMapClick(LatLng latLng)
        {
            Log.d(TAG, "onMapClick: " + latLng.toString());
            mMarkerManager.hideInfoWindow();
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi)
        {
            return false;
        }
    }
}

