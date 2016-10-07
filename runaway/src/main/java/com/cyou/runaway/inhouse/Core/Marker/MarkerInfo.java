package com.cyou.runaway.inhouse.Core.Marker;

/**
 * Created by Xiao on 2016/10/5.
 */

public class MarkerInfo
{
    public enum MarkerType
    {
        MT_MainPlayer,
        MT_Player,
        MT_School,
    }

    public MarkerInfo(MarkerType type, int id)
    {
        this.markType = type;
        this.id = id;
    }

    public MarkerType markType;
    public int id;
}
