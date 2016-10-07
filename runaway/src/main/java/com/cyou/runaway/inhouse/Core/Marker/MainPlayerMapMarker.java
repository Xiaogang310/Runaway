package com.cyou.runaway.inhouse.Core.Marker;

import com.baidu.mapapi.map.MarkerOptions;

/**
 * Created by Xiao on 2016/10/5.
 */

public class MainPlayerMapMarker extends MapMarker
{
    public MainPlayerMapMarker(MapMarkerManager mgr, MarkerInfo info)
    {
        super(mgr, info);
    }

    @Override
    protected void setupMarker()
    {
        mOptions = new MarkerOptions().position(mLocation).icon(mBitmapDescriptor).zIndex(mManager.depthMainPlayer);
        mMarker = mManager.addMarker(this);
    }
}
