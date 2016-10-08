package com.cyou.runaway.inhouse.Core.Marker;

import com.baidu.mapapi.map.MarkerOptions;

/**
 * Created by Xiao on 2016/10/6.
 */

public class MapMarkerSchool extends MapMarker
{
    public MapMarkerSchool(MapMarkerManager mgr, MarkerInfo info)
    {
        super(mgr, info);
    }

    @Override
    protected void setupMarker()
    {
        mOptions = new MarkerOptions().position(mLocation).icon(mBitmapDescriptor).zIndex(mManager.depthSchool);
        mMarker = mManager.addMarker(this);
    }
}
