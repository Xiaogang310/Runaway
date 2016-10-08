package com.cyou.runaway.inhouse.Core.Util;

import android.graphics.Point;

/**
 * Created by Gang on 2016/10/8.
 */

public class Helper
{
    //先确定marker的位置，在以屏幕中心为原点的那个象限里
    //根据象限来确定popup的位置，即marker在左上象限那么popup出现在marker的右下，
    //marker在左下象限那么popup出现在marker的右上
    //同时考虑marker和popup的大小
    public static Point CalculatePopupPosition(Point center, Point markerPosition, Point markerSize, Point popupSize)
    {
        int px = (markerPosition.x - center.x) > 0 ? -1 : 1;
        int py = (markerPosition.y - center.y) > 0 ? -1 : 1;

        int xPos = markerPosition.x + px * (markerSize.x + popupSize.x ) / 2;
        int yPos = markerPosition.y + popupSize.y /2 + py * (markerSize.y + popupSize.y) / 2;

        return new Point(xPos, yPos);
    }
}
