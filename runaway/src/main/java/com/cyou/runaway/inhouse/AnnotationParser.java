package com.cyou.runaway.inhouse;

/**
 * Created by Gang on 2016/10/10.
 * 工具类，用于生成用户API手册
 */

public class AnnotationParser
{
    protected String mPackageName;

    public AnnotationParser()
    {
        mPackageName = getClass().getPackage().getName();
    }

    public String getPackageName()
    {
        return mPackageName;
    }
}
