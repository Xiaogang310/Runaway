package com.cyou.runaway.inhouse.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cyou.runaway.inhouse.Component.Location.LocationService;
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
    protected int mRadiusIncrement = 0;
    protected boolean mFirstLocation = false;

    protected Marker mMarker;
    protected BitmapDescriptor mMakerADescriptor;

    LocationService mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFirstLocation = true;
        mLocationListener = new MapLocationListener();
        mMapView = new MapView(this, new BaiduMapOptions());
        mMap = mMapView.getMap();
        mMap.setMyLocationEnabled(true);

        mMakerADescriptor = BitmapDescriptorFactory.fromAsset("Icon-114.png");

        mLocationService = (LocationService) SDKContainer.getInstance().getComponent(LocationService.TAG);
        mLocationService.startWithListener(mLocationListener);

        initView(this);
        setContentView(mLayout);
        initOverlay();
    }

    protected void initOverlay()
    {
        LatLng llMaker = new LatLng(39.919224, 116.211888);

        MarkerOptions markerOptions = new MarkerOptions().position(llMaker).icon(mMakerADescriptor)
                .zIndex(9).draggable(true);

        markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);

        mMarker = (Marker)(mMap.addOverlay(markerOptions));
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

        mMapView.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    public class MapLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {

            if (null == bdLocation || null == mMapView)
                return;

            Log.d(TAG, "onReceiveLocation: map location");

            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius() + mRadiusIncrement)
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            mMap.setMyLocationData(locationData);

            if (mFirstLocation)
            {
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                MapStatus.Builder builder = new MapStatus.Builder();

                builder.target(ll).zoom(20.0f);
                mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                mFirstLocation = false;
            }

        }
    }

    public class MapClickListener implements BaiduMap.OnMapClickListener
    {
        @Override
        public void onMapClick(LatLng latLng)
        {
            mRadiusIncrement += 100;
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi)
        {
            return false;
        }
    }
}

