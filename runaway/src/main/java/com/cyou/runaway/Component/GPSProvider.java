package com.cyou.runaway.Component;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.cyou.runaway.SDKContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/22.
 */
public class GPSProvider implements ComponentInterface
{
    protected final String INVALID_PROVIDER = "INVALID_PROVIDER";
    protected LocationManager mLocationManager;
    protected LocationListener mLocationListener;


    public GPSProvider(LocationManager locMgr)
    {
        mLocationManager = locMgr;
        init();
    }

    public void start()
    {
        String provider = getLocationProvider();
        try
        {
            if (INVALID_PROVIDER != provider)
                mLocationManager.requestLocationUpdates(provider, 5, 5, mLocationListener);
            else
                providerInvalidCallback();
        }
        catch (SecurityException e)
        {
            permissionCallback(e.getMessage());
        }
    }

    public void stop()
    {
        try
        {
            mLocationManager.removeUpdates(mLocationListener);
        }
        catch (SecurityException e)
        {
            permissionCallback(e.getMessage());
        }

    }

    @Override
    public String toString()
    {
        return "gps";
    }

    protected void init()
    {
        mLocationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                locationChangedCallback(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };
    }

    protected String getLocationProvider()
    {
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return LocationManager.NETWORK_PROVIDER;
        else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return LocationManager.GPS_PROVIDER;
        else
            return INVALID_PROVIDER;
    }

    protected void locationChangedCallback(Location location)
    {
        try
        {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("lat", location.getLatitude());
            jsonObj.put("lng", location.getLongitude());

            SDKContainer.UnityCallback("GPSLocationCallabck", jsonObj.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    protected void providerInvalidCallback()
    {

    }

    protected void permissionCallback(String error)
    {

    }
}